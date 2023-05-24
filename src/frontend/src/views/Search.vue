<template>

 <!-- 매장 등록 모달창 -->
 <!-- StoreRegi 추후 Component화 예정 css 아직-->
  <transition name="fade">
    <div class="black-bg" v-if="modalOpen">
        <div class="white-bg">
          <form action="" enctype="multipart/form-data">
            <h4>센터 정보</h4>
            <label for="name">회사명</label>
            <input type="text" id="name" v-model="companyData.name"/><br>

            <label for="address">주소</label>
            <input type="text" id="address" v-model="companyData.address"/><br>

            <label for="info">매장 소개</label>
            <input type="text" id="info" v-model="companyData.info"/><br>

            <label for="phoneNumber">전화번호</label>
            <input type="text" id="phoneNumber" v-model="companyData.phoneNumber"/> <br>

            <label for="store">매장 사진</label>
            <input type="file" multiple name="storeFile" id="store" accept="image/*" @change="centerFileChange"/><br>
            
            <button type="submit" @click="centerTotalSubmit">보내기</button>
          </form>
          <button @click="modalOpen = false">모달창 닫기</button>
        </div>  
    </div>
  </transition>

  
  <!-- 매장검색 창 추후 components화 예정-->
  <div class="search-container">
    <h1>서치창</h1>
    <button @click="modalOpen = true">매장 등록</button>
    <button>매장정보 수정</button>
    <form>
      <label for="searchStore">매장검색</label>
      <input v-model="searchStore" id="searchStore">
      <button @click="searchStoreSubmit">검색</button>
    </form>

    
    
    <!-- 매장 검색 창 추후 components 화 예정-->
    <div>
      <h1>검색된 매장 내용</h1>
    </div>

  </div>  
</template>

<script setup>

import { ref, watchEffect } from 'vue';
import axios from 'axios';
const url = "http://localhost:8090/"


//center 가입 data
const companyData = ref({
  name: '',  
  address: '',
  phoneNumber: '',  
  info: '',  
});


//센터 이미지 aws 처리 로직
const centerImg= ref([]);

const centerFileChange = (e) => {
  const files = e.target.files;  
  for(let i = 0; i < files.length; i++){
    centerImg.value.push(files[i]);    
  }  
}

//aws 사진 처리 로직 + data 처리 로직
const centerTotalSubmit = async(e) => {
  e.preventDefault();

  const centerRigiData = new FormData();

  for(let i = 0; i < centerImg.value.length; i++){
    centerRigiData.append('centerImg', centerImg.value[i]);    
  }
  
  centerRigiData.append('name', companyData.value.name)
  centerRigiData.append('address', companyData.value.address)
  centerRigiData.append('phoneNumber', companyData.value.phoneNumber)
  centerRigiData.append('info', companyData.value.info)
  
  
  try{
    await axios.post(url+"center/infoInsert",centerRigiData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  console.log("야호 성공!");

  //성공 이후 로직
  //초기화
  companyData.value.name = '';
  companyData.value.address = '';
  companyData.value.phoneNumber = '';
  companyData.value.info = '';
  //이미지 처리 초기화
  centerImg.value = []
  modalOpen.value = false;
  //성공메시지
  const successInsertData = ref(true);
  } catch(error){
    console.log("다시와라 애송이");
    console.log(error);
  }
}



//단건
// const trainerImg= ref(null);
// const fileChange = (e) => {  
//   trainerImg.value = e.target.files[0];
// }
// 이하 진행용

const postCondition = ref(false)
const modalOpen = ref(false);
const searchStore = ref('');

const searchStoreSubmit = (e) => {
  e.preventDefault();

  //엑시오스 처리 
  
  searchStoreSubmit.value = '';
}

const onSubmit = (e) => {
  e.preventDefault(); 

  axios.post(url+"search/input",
  companyData.value
  ).then(res => {
    console.log(res.data);
  }).catch(error=>{
    console.log(error);
  })
  
}

//get구현
const getData = ref(null);

// watchEffect(async () => {
//   const response = await axios.get("/search");  
//   getData.value = await response.data;
//   console.log(getData.value); 
// })



//delete 구현





</script>

<style scoped>
  div{
    box-sizing: border-box;
  }

  .black-bg{
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    position: fixed;
    padding: 20px
  }
  .white-bg{
    width: 100%;
    background: white;
    border-radius: 30px;
    padding: 20px;
  }

  .fade-enter-from {
	opacity: 0;
}

.fade-enter-active{
	transition: all 1s;
}

.fade-enter-to {
	opacity: 1;
}

.search-container{
  margin-left: 8vh;
  margin-right: 8vh;  
  width: auto;
  height: auto;
  min-height: 100vh;
}
</style>