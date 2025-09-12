package co.com.bancolombia.r2dbc.sender;

import co.com.bancolombia.model.cola.MessageGateway;
import co.com.bancolombia.model.reportes.Reportes;
import co.com.bancolombia.model.solicitud.Solicitud;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class SqsMessageSender implements MessageGateway {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SqsAsyncClient sqsClient;
    private static final Logger logger = LoggerFactory.getLogger(SqsMessageSender.class);
    private static final String QUEUE_URL =
            "https://sqs.us-east-1.amazonaws.com/310070349151/cola-solicitudes";

    private static final String QUEUE_URL_CALCULO_CUOTA =
            "https://sqs.us-east-1.amazonaws.com/310070349151/cola-CalculoCuota";

    private static final String QUEUE_URL_MENSAJE_COUOTA =
            "https://sqs.us-east-1.amazonaws.com/310070349151/cola-respuesta-capacidad";

    private static final String QUEUE_URL_CANTIDAD_TOTAL =
            "https://sqs.us-east-1.amazonaws.com/310070349151/cola-cantidadYMontoTotal";

    @Override
    public Mono<Void> send(Solicitud solicitud) {
        try {

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("documento", solicitud.getDocumento());
            bodyMap.put("nombreEstado", solicitud.getNombreEstado());
            bodyMap.put("idSolicitud", solicitud.getIdSolicitud());

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(bodyMap);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(body)
                    .build();
            logger.info("***************************"); logger.info("Enviando Mensaje"); logger.info("*****************************");
            return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                    .then();
        } catch (Exception e) {
            logger.info("***************************"); logger.info("Error"); logger.info("*****************************");
            return Mono.error(e);
        }
    }

    public Mono<String> calcularCapacidadEndeudamiento(List<Solicitud> solicitudes) {
        try {
            List<Map<String, Object>> listaBody = solicitudes.stream().map(solicitud -> {
                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("documento", solicitud.getDocumento());
                bodyMap.put("nombreEstado", solicitud.getNombreEstado());
                bodyMap.put("idSolicitud", solicitud.getIdSolicitud());
                bodyMap.put("salario", solicitud.getSalario_base());
                bodyMap.put("monto", solicitud.getMonto());
                bodyMap.put("interes", solicitud.getTasa_interes());
                bodyMap.put("cuotas", solicitud.getPlazo());
                return bodyMap;
            }).toList();

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(listaBody);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL_CALCULO_CUOTA)
                    .messageBody(body)
                    .build();

            logger.info("***************************");
            logger.info("***************************"); logger.info("Enviando un solo mensaje con " + solicitudes.size() + " solicitudes");

            return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                    .map(response -> "Mensaje enviado con " + solicitudes.size() + " solicitudes");

        } catch (Exception e) {
            logger.error("Error al enviar el mensaje con todas las solicitudes", e);
            return Mono.error(e);
        }
    }


    public Mono<String> recibirRespuestaLambda() {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL_MENSAJE_COUOTA)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(5)
                .build();

        return Mono.fromFuture(() -> sqsClient.receiveMessage(receiveRequest))
                .map(response -> {
                    List<Message> messages = response.messages();
                    for (Message msg : messages)
                    {
                        logger.info("Mensaje recibido->: " + msg.body());
                    }
                    return "Return Mensajes recibidos: " + messages.size();
                });
    }


    @Override
    public Mono<Void> sendUpdate(Solicitud solicitud) {
        try {

            long metricaAleatoria = ThreadLocalRandom.current().nextLong(1, 10000001);

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("metrica", metricaAleatoria);
            bodyMap.put("monto", solicitud.getMonto());

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(bodyMap);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL_CANTIDAD_TOTAL)
                    .messageBody(body)
                    .build();
            logger.info("***************************"); logger.info("Enviando datos a DinamoDB"); logger.info("*****************************");
            return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                    .then();
        } catch (Exception e) {
            logger.info("***************************"); logger.info("Error"); logger.info("*****************************");
            return Mono.error(e);
        }
    }

    public Flux<String> recibirTodosMensajes() {
        return Flux.defer(() -> {
                    ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                            .queueUrl(QUEUE_URL_CANTIDAD_TOTAL)
                            .maxNumberOfMessages(10)
                            .waitTimeSeconds(10)
                            .build();
                    logger.info("***************************"); logger.info("recibirTodosMensajes"); logger.info("*****************************");
                    return Mono.fromFuture(() -> sqsClient.receiveMessage(request))
                            .flatMapMany(resp -> Flux.fromIterable(resp.messages())
                                    .map(Message::body));
                });
    }
}
