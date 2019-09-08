package com.au.example.proxy.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class ProxyLogMapper {

    public <T,K>  MapperFacade getMapperFacade(Class<T> t, Class<K> k){
        MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(t,k);
        return  mapperFactory.getMapperFacade();
    }

}
