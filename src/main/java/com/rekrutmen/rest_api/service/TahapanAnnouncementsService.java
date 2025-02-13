package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.TahapanAnnouncements;
import com.rekrutmen.rest_api.repository.TahapanAnnouncementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TahapanAnnouncementsService {

    private final TahapanAnnouncementsRepository repository;

    @Autowired
    public TahapanAnnouncementsService(TahapanAnnouncementsRepository repository) {
        this.repository = repository;
    }

    public TahapanAnnouncements getLatestContentByIdLowongan(Integer idLowongan) {
        return repository.findLatestContentByIdLowongan(idLowongan);
    }
}