<template>
  <Toast :message="message" :isToastVisible="isToastVisible"></Toast>
  <div class="div_container">
    <!-- 매장 등록 컴포넌트 -->
    <transition name="insertCenter">
      <Register
      v-if="centerRegisterModal"
      :centerRegisterModal="centerRegisterModal"
      @closeCenterRegisterModal="closeCenterRegisterModal"
      v-model="message"
      ></Register>
    </transition>
    <!-- 매장 수정 컴포넌트 -->
    <transition name="modifyCenter">
      <Modify
        v-if="centerModifyModal"
        :centerModifyModal="centerModifyModal"
        @closeCenterModifyModal="closeCenterModifyModal"
        v-model="message"
        @showToast="showToast"
      ></Modify>
    </transition>

    <!-- 트레이너 정보등록 컴포넌트 -->
    <transition name="trainerRegister">
      <TrainerRegister v-if="trainerRegisterModal"
      :trainerRegisterModal="trainerRegisterModal"
      @closeTrainerRegisterModal="closeTrainerRegisterModal"
      />
    </transition>

    <TrainerModify />


    <!-- 센터 버튼 -->
    <div class="center_btn">      
      <button @click="centerRegisterModal = true">매장등록컴포넌트</button>      <!-- 매장 정보 수정 컴포넌트 -->
      <button @click="centerModifyModal = true">매장수정컴포넌트</button>
    </div>
    
    <!-- 트레이너 버튼 -->
    <div class="trainer_btn">
      <button @click="trainerRegisterModal = true">트레이너 정보 등록</button>
      <button>트레이너 정보 수정</button>
    </div>

    <h3>{{ message }}ㅈㅈㅈㅈㅈㅈㅈㅈ</h3>

    <!-- 서치 컴포넌트 -->
    <Search></Search>

    <!-- 매장검색 창 추후 components화 예정-->
    <div class="search-container">
      <h1>서치창</h1>

      <form>
        <label for="searchStore">매장검색</label>
        <input v-model="searchStore" id="searchStore" />
        <button @click="searchStoreSubmit">검색</button>
      </form>

      <!-- 매장 검색 창 추후 components 화 예정-->
      <div>
        <h1>검색된 매장 내용</h1>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

import Register from "../components/center/container/Register.vue";
import Modify from "../components/center/container/Modify.vue";
import Toast from "../components/center/ui/Toast.vue";
import Search from "../components/center/container/Search.vue";
import TrainerRegister from "../components/center/container/TrainerRegister.vue";
import TrainerModify from "../components/center/container/TrainerModify.vue";

import axios from "axios";

//Toast 를 위한 양방향 props
const message = ref("");
const isToastVisible = ref(false);
// console.log(message);
let timeOut = null;
const showToast = () => {
  isToastVisible.value = true;

  timeOut = setTimeout(() => {
    isToastVisible.value = false;
    clearTimeout(timeOut);
  }, 1300);
};

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


const searchStore = ref("");

const searchStoreSubmit = (e) => {
  e.preventDefault();

  //엑시오스 처리

  searchStoreSubmit.value = "";
};

const onSubmit = (e) => {
  e.preventDefault();

  axios
    .post(url + "search/input", companyData.value)
    .then((res) => {
      console.log(res.data);
    })
    .catch((error) => {
      console.log(error);
    });
};
</script>

<style scoped>
h1 {
  color: black;
}

div {
  box-sizing: border-box;
}

.div_container {
  margin-left: 8vh;
  margin-right: 8vh;
  width: auto;
  height: auto;
  min-height: 100vh;
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
.insertCenter-enter-from {
  opacity: 0;
}

.insertCenter-enter-active {
  transition: all 1s;
}

.insertCenter-enter-to {
  opacity: 1;
}

.modifyCenter-enter-from {
  opacity: 0;
}
.modifyCenter-enter-active {
  transition: all 1.0s;
}

.modifyCenter-enter-to {
  opacity: 1;
}

.trainerRegister-enter-from{
  opacity: 0;
}

.trainerRegister-enter-active{
  transition: all 1.0s;
}

.trainerRegister-enter-to{
  opacity: 1;
}

</style>