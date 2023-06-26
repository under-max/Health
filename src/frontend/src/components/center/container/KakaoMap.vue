<template>
    <div class="mapContainer">            
        <div id="map" style="width:93vw;height:40vh"></div>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { defineProps } from 'vue';
  
  const props = defineProps({
    centerDetail: Object,
    
  });

  
  const centerDetail = props.centerDetail;

  const checkAddress = "서울특별시 서대문구 명물길 16"

  const map = ref(null);
  let geocoder;
  
  const initMap = () => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), //기본값
      level: 1,
    };
    map.value = new kakao.maps.Map(container, options);
  
    geocoder = new kakao.maps.services.Geocoder();
    const address = `${centerDetail.address}`;
    

    geocoder.addressSearch(address, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
  
        const marker = new kakao.maps.Marker({
          map: map.value,
          position: coords,
        });
  
        const infowindow = new kakao.maps.InfoWindow({
          content: `<div style="width:150px;text-align:center;padding:6px 0;">${centerDetail.name}</div>`,
        });
  
        infowindow.open(map.value, marker);
        map.value.setCenter(coords);
      }
    });
  };
  
  onMounted(() => {
    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=c9ad4abe859c5f2466b65ffb2a4ba497&libraries=services";
      script.addEventListener("load", () => {
        console.log("loaded", window.kakao);
        window.kakao.maps.load(initMap);
      });
      document.head.appendChild(script);
    } else {
      console.log("이미 로딩됨");
      initMap();
    }
  });
  </script>

<style scoped>
.mapContainer{
    width: 100%;
    margin-top: 2vh;
    margin-bottom: 2vh;
}

.mapContainer div{
    margin-left: auto;
    margin-right: auto;
    border-radius: 20px;
}

.mapContainer h4{
    text-align: center;
    font-size: 1.4rem;
}
</style>