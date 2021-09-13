package com.simplify.marketplace.service.impl;

import com.simplify.marketplace.domain.Certificate;
import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.repository.CertificateRepository;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.CertificateService;
import com.simplify.marketplace.service.dto.CertificateDTO;
import com.simplify.marketplace.service.mapper.CertificateMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Certificate}.
 */
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    ESearchWorkerRepository ewrep;

    @Autowired
    WorkerRepository wrepo;

    private final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

    private final CertificateRepository certificateRepository;

    private final CertificateMapper certificateMapper;

    public CertificateServiceImpl(CertificateRepository certificateRepository, CertificateMapper certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {
        log.debug("Request to save Certificate : {}", certificateDTO);
        Certificate certificate = certificateMapper.toEntity(certificateDTO);
        certificate = certificateRepository.save(certificate);
        return certificateMapper.toDto(certificate);
    }

    @Override
    public Optional<CertificateDTO> partialUpdate(CertificateDTO certificateDTO) {
        log.debug("Request to partially update Certificate : {}", certificateDTO);

        return certificateRepository
            .findById(certificateDTO.getId())
            .map(
                existingCertificate -> {
                    certificateMapper.partialUpdate(existingCertificate, certificateDTO);

                    return existingCertificate;
                }
            )
            .map(certificateRepository::save)
            .map(certificateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Certificates");
        return certificateRepository.findAll(pageable).map(certificateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificateDTO> findOne(Long id) {
        log.debug("Request to get Certificate : {}", id);
        return certificateRepository.findById(id).map(certificateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificate> findOneWorker(Long id) {
        log.debug("Request to get Certificate : {}", id);
        return certificateRepository.findByWorkerId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Certificate : {}", id);
        certificateRepository.deleteById(id);
    }

    public Set<Certificate> insertElasticSearch(CertificateDTO certificateDTO) {
        String workerid = certificateDTO.getWorker().getId().toString();
        ElasticWorker elasticworker = ewrep.findById(workerid).get();

        Certificate certificate = new Certificate();

        certificate.setCertificateName(certificateDTO.getCertificateName());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setExpiryYear(certificateDTO.getExpiryYear());
        certificate.setId(certificateDTO.getId());
        certificate.setIssuer(certificateDTO.getIssuer());
        certificate.setIssueYear(certificateDTO.getIssueYear());
        certificate.setWorker(wrepo.findById(certificateDTO.getWorker().getId()).get());

        Set<Certificate> certSet = elasticworker.getCertificates();
        certSet.add(certificate);

        return certSet;
    }
}
