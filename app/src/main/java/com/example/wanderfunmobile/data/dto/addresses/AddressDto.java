package com.example.wanderfunmobile.data.dto.addresses;

public class AddressDto {
    private Long id;
    private ProvinceDto province;
    private DistrictDto district;
    private WardDto ward;
    private String street;

    public AddressDto() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public WardDto getWard() {
        return ward;
    }

    public void setWard(WardDto ward) {
        this.ward = ward;
    }

    public DistrictDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDto district) {
        this.district = district;
    }

    public ProvinceDto getProvince() {
        return province;
    }

    public void setProvince(ProvinceDto province) {
        this.province = province;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
