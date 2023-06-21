<template>
  <div class="centerRegisterContainer">
    <div class="centerRegister" v-if="isRegister">
        <CenterRegister :isRegister="isRegister" @isModify="isModify"/>
    </div>
    <div class="centerModify" v-else>
        <CenterModify :isRegister="isRegister" @isModify="isModify"/>
    </div>
  </div>
</template>

<script setup>
import CenterRegister from '../components/center/container/CenterRegister.vue';
import CenterModify from '../components/center/container/CenterModify.vue';
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
  console.log(centerToken);  
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
})

//상태확인
const isRegister = ref(true);

const isModify = () => {
    isRegister.value = !isRegister.value;    
}
</script>

<style scoped>
h1 {
  color: black;
}

.centerRegisterContainer {
    min-height: 91.2vh;
    height: auto;
    background: white;
}

.centerRegister {
    margin: 0 auto;
    padding: 20px;
    min-width: 600px;
    width: 65%;
}

.centerModify {
    margin: 0 auto;
    padding: 20px;
    min-width: 600px;
    width: 45%;
}
</style>