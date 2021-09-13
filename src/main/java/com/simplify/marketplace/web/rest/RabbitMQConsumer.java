package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
//import com.simplify.marketplace.domain.ElasticWorker;
//import com.simplify.marketplace.domain.Worker;
//import com.simplify.marketplace.repository.ESearchWorkerRepository;
//import com.simplify.marketplace.repository.WorkerDTORepo;
//import com.simplify.marketplace.service.dto.WorkerDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    ElasticWorker e;

    @Autowired
    ESearchWorkerRepository rep1;

    @RabbitListener(queues = "queue1")
    public void comsume(ElasticWorker w) {
        rep1.save(w);
    }
}
