package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.response.LicaoDtoResponse;
import com.senac.conectIdade.dto.response.ModuloDtoResponse;
import com.senac.conectIdade.entity.Licao;
import com.senac.conectIdade.entity.Modulo;
import com.senac.conectIdade.repository.LicaoRepository;
import com.senac.conectIdade.repository.ModuloRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final LicaoRepository licaoRepository;
    private final ModelMapper modelMapper;

    public ModuloService(ModuloRepository moduloRepository, LicaoRepository licaoRepository, ModelMapper modelMapper) {
        this.moduloRepository = moduloRepository;
        this.licaoRepository = licaoRepository;
        this.modelMapper = modelMapper;
    }

    public List<ModuloDtoResponse> listarModulos() {
        List<Modulo> modulos = moduloRepository.findAllByOrderByOrdemAsc();
        return modulos.stream()
                .map(modulo -> modelMapper.map(modulo, ModuloDtoResponse.class))
                .collect(Collectors.toList());
    }

    public List<LicaoDtoResponse> listarLicoesPorModulo(Integer moduloId) {
        List<Licao> licoes = licaoRepository.findByModuloId(moduloId);
        return licoes.stream()
                .map(licao -> modelMapper.map(licao, LicaoDtoResponse.class))
                .collect(Collectors.toList());
    }
}