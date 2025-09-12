package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.cola.MessageGateway;
import co.com.bancolombia.model.reportes.Reportes;
import co.com.bancolombia.model.reportes.gateways.ReportesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class ReporteRepositoryAdapter implements ReportesRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageGateway messageGateway;
    private final DynamoDbAsyncClient dynamoDbClient;

    public ReporteRepositoryAdapter(MessageGateway messageGateway, DynamoDbAsyncClient  dynamoDbClient) {
        this.messageGateway = messageGateway;
        this.dynamoDbClient = dynamoDbClient;
    }


    @Override
    public Mono<Reportes> saveDinamo() {

        return messageGateway.recibirTodosMensajes()
                .flatMap(mensaje -> {
                    try {
                        Reportes reporte = mapper.readValue(mensaje, Reportes.class);

                        Map<String, AttributeValue> item = new HashMap<>();
                        item.put("metrica", AttributeValue.builder().n(String.valueOf(reporte.getMetrica())).build());
                        item.put("monto", AttributeValue.builder().n(String.valueOf(reporte.getMonto())).build());

                        PutItemRequest request = PutItemRequest.builder()
                                .tableName("reporte_aprobados")
                                .item(item)
                                .build();

                        return Mono.fromFuture(() -> dynamoDbClient.putItem(request))
                                .thenReturn(reporte);

                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error parseando mensaje individual: " + mensaje, e));
                    }
                })
                .then(obtenerSumaMontos());
    }


    public Mono<Reportes> obtenerSumaMontos() {
        ScanRequest request = ScanRequest.builder()
                .tableName("reporte_aprobados")
                .build();

        return Mono.fromFuture(() -> dynamoDbClient.scan(request))
                .flatMapMany(response -> Flux.fromIterable(response.items()))
                .collectList()
                .map(items -> {
                    long totalMonto = items.stream()
                            .mapToLong(item -> Long.parseLong(item.get("monto").n()))
                            .sum();

                    long totalPrestamos = items.size();

                    return Reportes.builder()
                            .prestamosAprobados(totalPrestamos)
                            .monto(totalMonto)
                            .build();
                });
    }





}
