package io.github.amsatrio.dao;

import javax.enterprise.context.ApplicationScoped;

import io.github.amsatrio.dto.HelloWorldDto;

@ApplicationScoped
public class HelloWorldDao {
    HelloWorldDto helloWorldDto = new HelloWorldDto();
    public HelloWorldDto find(){
        return helloWorldDto;
    }
    public HelloWorldDto update(HelloWorldDto data){
        helloWorldDto.setMessage(data.getMessage());
        return helloWorldDto;
    }
}
