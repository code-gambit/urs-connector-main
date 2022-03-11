package com.urlshortner.urlshortner.Controller;

import com.urlshortner.discovery.ServiceInstanceMapper;
import com.urlshortner.discovery.Serviceregistry;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceController.PREFIX)
public class ServiceController {
    public static final String PREFIX="/instances";
    private Serviceregistry serviceregistry;

    public ServiceController(Serviceregistry serviceregistry) {
        this.serviceregistry = serviceregistry;
    }

    @GetMapping(value = "/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public String getInstance(@PathVariable String name){
        ServiceInstanceMapper temp=serviceregistry.getInstance(name);
        return temp.getServiceInstanceId();
    }

}
