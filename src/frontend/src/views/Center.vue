<template>
    <div class="div_container">

    <!-- admin만 가능 -->
    <div v-if="isAdmin">
      <div class="adminRegister">
        <button class="centerRegister" @click="goToCenterRegister">센터 등록</button>
        <button class="trainerRegistera" @click="goToTrainerRegister">트레이너 등록</button>
        
      </div>
    </div>

    <!-- 센터 search -->
    <div class="centerListSearch" @keyup.enter="centerSearchBtn">
      <input type="text" v-model="centerSearchValue">
      <select name="" id="" v-model="centerSearchType">
        <option value="centerName" selected>센터명</option>
        <option value="address">주소</option>
      </select>
      <button @click="centerSearchBtn">검색</button>
    </div>
    
    
    <!-- 센터리스트-->
    <div class="centerContainers" v-if="!isSearchCenter">
      <template v-for="(data, index) in centerList" :key="index">
        <CenterList v-if="centerList" :centerList="data" @click="goToDetail(data.id)"></CenterList>
      </template>      
    </div>
    <!-- 검색했을때 -->
    <div class="centerContainers" v-else>
      <template v-for="(data, index) in centerSearchData" :key="index">
        <CenterList v-if="centerSearchData" :centerList="data" @click="goToDetail(data.id)"></CenterList>
      </template>      
    </div>

    <!-- 더보기 버튼 -->
    <div class="plusButtonDiv" v-if="!isSearchCenter">
      <hr>
      <button class="plusButton" @click="addList">더보기</button>
    </div>
    <!-- 검색했을때 더보기 버튼 -->
    <div class="plusButtonDiv" v-else>
      <hr>
      <button class="plusButton" @click="addSearchList">더보기</button>
    </div>


  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import Cookies from "vue-cookies";
import {showCustomAlert} from "../components/center/ui/Toast";

import CenterList from "../components/center/container/CenterList.vue";

import axios from "axios";

//라우터 정리
const router = useRouter();
//라우터 이동함수
const goToDetail = (id) => {
  router.push({name:'centerDetail', params: {id: id}})
}

//관리자 트레이너등록 페이지 이동
const goToTrainerRegister = () => {
  router.push('/center/trainerRegister');
}
//관리자 센터등록 페이지 이동
const goToCenterRegister = () => {
  router.push('/center/centerRegister')
}

//user 확인
const user = ref(null);
const centerToken = Cookies.get("accessToken")
const tokenCheck = async() => {  
  try{
    const response = await axios.get(url+"center/checkToken",{
      headers:{
        Authorization: centerToken,
      }
    })
    user.value = response.data;
    if(user.value.authority === 3){
    isAdmin.value = true;    
   }

  } catch(error){
    console.log(error);
  }
}
//Admin check
const isAdmin = ref(false);
onMounted(()=>{
  if(centerToken !== null){
    tokenCheck();
  }
})

// url
const url = "http://localhost:8090/";

// Components Register Modal 로직
const centerRegisterModal = ref(false);

const closeCenterRegisterModal = () => {
  centerRegisterModal.value = false;
};

// Components Modify Modal 로직
const centerModifyModal = ref(false);

const closeCenterModifyModal = () => {
  centerModifyModal.value = false;
};

//Components Trainer Register Modal 로직
const trainerRegisterModal = ref(false);

const closeTrainerRegisterModal = () => {
  trainerRegisterModal.value = false;
}
//Components Trainer Modify Modal 로직
const trainerModifyModal = ref(false);

const closeTrainerModifyModal = () => {
  trainerModifyModal.value = false;
}


//전체 center List 저장용
const centerList = ref([]);

//List 계산용
const limit = ref(6);
const offset = ref(0);

const centerCount = ref(0);
//onMounte로 centerList 출력
const isLoadingCenter = ref(true);

//allCenterList 가져오기
const allGetCenterList = async() => {
  try{
    const response = await axios.get(url+"center/allCenterList",{
      params:{
        limit: limit.value,
        offset: offset.value,
      }
    });
    centerList.value = response.data.center;
    centerCount.value = response.data.centerCount;
    centerCount.value = Math.floor(response.data.centerCount/6);
    
    isLoadingCenter.value = false;

    //리스트 계산 처리
  }catch(error){
    console.log(error);
  }
}
onMounted(async()=>{
  allGetCenterList();
  console.log("hi");
});

//센터 검색 처리
//검색타입
const centerSearchType = ref('centerName');
//검색 값
const centerSearchValue = ref('');
//offset 처리
const centerSearchOffset = ref(0);

//데이터 길이 처리
const centerSearchLength = ref(0);
//센터 저장 배열 처리
const centerSearchData = ref([]);
//검색 후 상황인지 확인
const isSearchCenter = ref(false);

//재검색인지 확인
const isNotFirstSearch = ref(false);

//센터 검색 
const centerSearchBtn = async(e) => {
  e.preventDefault();
  isSearchCenter.value = true;
  
  if(isNotFirstSearch){
    centerSearchOffset.value = 0;
    isNotFirstSearch.value = false;    
  }
  isNotFirstSearch.value = true;

  try{
    const response = await axios.get(url+"center/centerSearchByType",{
      params:{
        searchType: centerSearchType.value,
        searchValue: centerSearchValue.value,
        offset: centerSearchOffset.value
      }
    })

    let message = response.data.searchCount + "개의 센터를 찾았습니다."
    showCustomAlert(message);

    centerSearchLength.value = Math.floor(response.data.searchCount /3);
    centerSearchData.value = response.data.centerSearchList;
    console.log(centerSearchData.value);
    console.log(centerSearchLength.value);
  }catch(error){
    console.log(error);
  }
}
// 센터 검색 더보기 
const addSearchList = async(e) => {
  e.preventDefault();
  if(centerSearchLength.value !== 0){
    centerSearchOffset.value = centerSearchOffset.value + 3;
    try{
      const response = await axios.get(url+"center/centerSearchByType",{
        params:{
          searchType: centerSearchType.value,
          searchValue: centerSearchValue.value,
          offset: centerSearchOffset.value
        }
      })
  
      centerSearchLength.value = centerSearchLength.value - 1;
      centerSearchData.value = [...centerSearchData.value,...response.data.centerSearchList];
      console.log(centerSearchData.value);
      console.log(centerSearchLength.value);
    }catch(error){
      console.log(error);
    }
  }else{
    showCustomAlert("마지막 페이지 입니다.");
  }

}


//더보기 버튼 처리
const addList = async() => {
  if(centerCount.value != 0){    
    offset.value = offset.value + 6;
    try{
      const response = await axios.get(url+"center/allCenterList",{
        params:{
          limit: limit.value,
          offset: offset.value,
        }
      });
      centerList.value = [...centerList.value, ...response.data.center];      
      centerCount.value = centerCount.value - 1;      
      isLoadingCenter.value = false;
  
      //리스트 계산 처리
    }catch(error){
      console.log(error);
    }
  }else{    
    showCustomAlert("마지막 페이지 입니다.");
  }
}

</script>

<style scoped>
h1 {
  color: black;
}

div {
  box-sizing: border-box;
}

.div_container {
  margin-top: 3vh;
  margin-left: 8vh;
  margin-right: 8vh;
  width: auto;
  height: auto;
  /* min-height: 100vh; */
  min-width: 480px;
}

.center_btn {
  margin: 3vh;
  height: 5vh;
  display: flex;
  justify-content: flex-end;
}

.trainer_btn {
  margin: 3vh;
  height: 5vh;
  display: flex;
  justify-content: flex-end;
}

.centerContainers{    
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
}
@media (max-width: 800px) {
  .centerContainers {
    justify-content: center;
  }
}

.plusButtonDiv{
  text-align: center;
}
.plusButton{
  height: 65px;
  text-align: center;
  border-radius: 50px;
  margin-left: 0px;
  padding: 15px 61px;
  font-size: 23px;
  color: white;
  background: rgba(255, 130, 0, 0.9);
  border: none;
}



.centerListSearch {
  display: flex;
  align-items: center;
  margin-right: 4vw;
  margin-bottom: 2vh;
  justify-content: right;
}

.centerListSearch input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.centerListSearch select {
  margin-left: 8px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.centerListSearch button {
  margin-left: 5px;
  padding: 8px 16px;
  background-color: #2196f3;
  border: none;
  border-radius: 5px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.centerListSearch button:hover {
  background-color: #23527c;
}



.adminRegister{
  display: flex
}
.adminRegister .centerRegister{
  margin-left: 4vw;
}
.adminRegister button {
  margin-left: 10px;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.centerRegister{
  background-color: #2196f3;
  width: auto;
  color: white;
  transition: background-color 0.3s ease;
}

.centerRegister:hover {
  background-color: #23527c;
}

.trainerRegistera{
  background-color: yellow;
  width: auto;
  transition: background-color 0.3s ease;
}

.trainerRegistera:hover{
  background-color: #ffffcc;
  width: auto;
}

</style>