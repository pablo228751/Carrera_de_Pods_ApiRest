package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntenaService {

    @Autowired
    private AntenaRepository antenaRepository;
    @Autowired
    private IMapper<AntenaInDTO, Antena> antenaMapper;

    public void saveAntenas(List<AntenaInDTO> antenaInDTOS) {
        List<Antena> antenas = antenaInDTOS.stream()
                .map(antenaMapper::mapToEntity)
                .collect(Collectors.toList());
        antenaRepository.saveAll(antenas);
    }
}
