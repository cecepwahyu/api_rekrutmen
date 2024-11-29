package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

public class EditProfileRequest {

    // Profile data
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private byte[] profilePicture;

    // Family (Kerabat) data
    private List<KerabatRequest> kerabat;

    // Education (Riwayat Pendidikan) data
    private List<RiwayatPendidikanRequest> riwayatPendidikan;

    private List<RiwayatOrganisasiRequest> riwayatOrganisasi;

    private List<PengalamanKerjaRequest> pengalamanKerja;

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public byte[] getProfilePicture() {
//        return profilePicture;
//    }
//
//    public void setProfilePicture(String profilePicture) {
//        this.profilePicture = profilePicture != null
//                ? Base64.getDecoder().decode(profilePicture)
//                : null;
//    }

    public List<KerabatRequest> getKerabat() {
        return kerabat;
    }

    public void setFamily(List<KerabatRequest> kerabat) {
        this.kerabat = kerabat;
    }

    public List<RiwayatPendidikanRequest> getRiwayatPendidikan() {
        return riwayatPendidikan;
    }

    public void setRiwayatPendidikan(List<RiwayatPendidikanRequest> riwayatPendidikan) {
        this.riwayatPendidikan = riwayatPendidikan;
    }

    public List<RiwayatOrganisasiRequest> getRiwayatOrganisasi() {
        return riwayatOrganisasi;
    }

    public void setRiwayatOrganisasi(List<RiwayatOrganisasiRequest> riwayatOrganisasi) {
        this.riwayatOrganisasi = riwayatOrganisasi;
    }

    public List<PengalamanKerjaRequest> getPengalamanKerja() {
        return pengalamanKerja;
    }

    public void setPengalamanKerja(List<PengalamanKerjaRequest> pengalamanKerja) {
        this.pengalamanKerja = pengalamanKerja;
    }

    // Inner class for KerabatRequest
    public static class KerabatRequest {
        private String name;
        private String relationship;
        private String phoneNumber;
        private String email;
        private String address;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    // Inner class for EducationRequest
    public static class RiwayatPendidikanRequest {
        private String institutionName;
        private String degree;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String achievements;

        // Getters and setters
        public String getInstitutionName() {
            return institutionName;
        }

        public void setInstitutionName(String institutionName) {
            this.institutionName = institutionName;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getFieldOfStudy() {
            return fieldOfStudy;
        }

        public void setFieldOfStudy(String fieldOfStudy) {
            this.fieldOfStudy = fieldOfStudy;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getAchievements() {
            return achievements;
        }

        public void setAchievements(String achievements) {
            this.achievements = achievements;
        }
    }

    // Inner class for OrganizationRequest
    public static class RiwayatOrganisasiRequest {
        private String organizationName;
        private String role;
        private LocalDate startDate;
        private LocalDate endDate;
        private String responsibilities;

        // Getters and setters
        public String getOrganizationName() {
            return organizationName;
        }

        public void setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getResponsibilities() {
            return responsibilities;
        }

        public void setResponsibilities(String responsibilities) {
            this.responsibilities = responsibilities;
        }
    }

    public static class PengalamanKerjaRequest {
        @Getter
        @Setter
        private String companyName;

        @Getter
        @Setter
        private String position;

        @Getter
        @Setter
        private LocalDate startDate;

        @Getter
        @Setter
        private LocalDate endDate;

        @Getter
        @Setter
        private String responsibilities;


    }

}
