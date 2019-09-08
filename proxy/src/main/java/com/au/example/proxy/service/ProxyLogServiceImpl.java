package com.au.example.proxy.service;

import com.au.example.proxy.mapper.ProxyLogMapper;
import com.au.example.proxy.model.Header;
import com.au.example.proxy.model.ProxyLog;
import com.au.example.proxy.repository.ProxyLogRepository;
import com.au.example.proxy.service.dto.HeaderDto;
import com.au.example.proxy.service.dto.ProxyLogDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProxyLogServiceImpl implements ProxyLogService {

    @NonNull
    private final ProxyLogRepository proxyLogRepository;

    @NonNull
    private final ProxyLogMapper proxyLogMapper;

    @Override
    public void save(ProxyLogDto proxyLogDto) {
        ProxyLog proxyLog = proxyLogMapper.getMapperFacade(ProxyLogDto.class, ProxyLog.class).map(proxyLogDto, ProxyLog.class);
        proxyLogDto.getHeaders().stream().map(headerDto -> proxyLogMapper.getMapperFacade(HeaderDto.class, Header.class).
                map(headerDto,Header.class)).collect(Collectors.toSet());
        proxyLog.setHeaders(proxyLogDto.getHeaders().
                stream().
                map(headerDto -> proxyLogMapper.getMapperFacade(HeaderDto.class, Header.class).
                        map(headerDto,Header.class)).collect(Collectors.toSet()));
        proxyLogRepository.save(proxyLog);
    }
}
