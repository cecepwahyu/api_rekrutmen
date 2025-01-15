package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.TahapanAnnouncements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TahapanAnnouncementsRepository extends JpaRepository<TahapanAnnouncements, Integer> {

    @Query("SELECT t.content FROM TahapanAnnouncements t WHERE t.idLowongan = :idLowongan")
    String findContentByIdLowongan(@Param("idLowongan") Integer idLowongan);

    @Query(value = """
    SELECT *
    FROM tahapan_announcements
    WHERE id_lowongan = :idLowongan
      AND id_announcement = (
          SELECT MAX(id_announcement)
          FROM tahapan_announcements
          WHERE id_lowongan = :idLowongan
            AND approved = true
            AND status = 'P'
      )
    """, nativeQuery = true)
    TahapanAnnouncements findLatestContentByIdLowongan(@Param("idLowongan") Integer idLowongan);

}