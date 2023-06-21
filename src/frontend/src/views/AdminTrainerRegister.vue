<template>
    <div class="trainerRegisterContainer">
      <div class="trainerRegister" v-if="isRegister">
        <TrainerRegister :isRegister="isRegister" @isModify="isModify"/>
      </div>
      <div class="trainerModify" v-else>
        <TrainerModify :isRegister="isRegister" @isModify="isModify"/>
      </div>
  </div>
</template>

<script setup>
import TrainerRegister from '../components/center/container/TrainerRegister.vue';
import TrainerModify from '../components/center/container/TrainerModify.vue';
import { useRouter } from 'vue-router';
import {showCustomAlert} from "../components/center/ui/Toast";
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Cookies from "vue-cookies";


// url
const url = "http://localhost:8090/";

const router = useRouter();
//리스트로 리다이랙트
const centerRedirect = () => {
    router.push("/center")
}

const loginRedirect = () => {
  router.push("/login")
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
    if(user.value.authority !== 3){
        showCustomAlert("권한이 없습니다.");
        centerRedirect();
    }    
  } catch(error){
    console.log(error);
  }
}

onMounted(()=>{
  if(centerToken !== null){
    tokenCheck();  
  }else{
    loginRedirect();
  }  
});

//상태확인
const isRegister = ref(true);

const isModify = () => {
    isRegister.value = !isRegister.value;    
}
</script>

<style>
h1 {
  color: black;
}

.trainerRegisterContainer {
    min-height: calc(100vh - 81.51px);
    height: auto;
    min-width: 700px;
    height: auto;
    background: white;
}

.trainerRegister {
    margin-left: auto;
    margin-right: auto;
    padding-top: 20px;
    min-width: 65%;
    width: 65%;
}


.trainerModify {
    margin-left: auto;
    margin-right: auto;
    padding-top: 20px;
    min-width: 600px;
    width: 65%;
}
</style>