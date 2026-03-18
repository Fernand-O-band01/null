package com.example.demo.message;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data MongoDB para
 * la gestión de la entidad Message.
 * Proporciona los métodos automáticos para
 * consultar el historial de mensajes
 * tanto en conversaciones privadas como
 * en canales de servidores.
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    /**
     * Busca todos los mensajes que pertenecen a
     * una conversación privada (MD) específica.
     * Los resultados se devuelven ordenados
     * cronológicamente por su marca de tiempo (timestamp)
     * de forma ascendente (del más antiguo al más reciente).
     *
     * @param conversationId El identificador único
     * de la conversación privada.
     * @return Una lista de mensajes ordenados
     * por fecha pertenecientes a la conversación.
     */
    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);

    /**
     * Busca todos los mensajes que se han enviado
     * a un canal público de servidor específico.
     * Los resultados se devuelven ordenados
     * cronológicamente por su marca de tiempo (timestamp)
     * de forma ascendente (del más antiguo al más reciente).
     *
     * @param channelId El identificador único del
     * canal del servidor.
     * @return Una lista de mensajes ordenados por
     * fecha pertenecientes al canal.
     */
    List<Message> findByChannelIdOrderByTimestampAsc(Long channelId);
}
