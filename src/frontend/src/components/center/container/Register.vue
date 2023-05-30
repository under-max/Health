<template>
  <transition name="insertCenter">
    <div class="black-bg">
      <Toast :message="message" :isToastVisible="isToastVisible"></Toast>
      <h1>aaaa</h1>
        <div class="white-bg">
          <form action="" enctype="multipart/form-data">
            <h4>센터 정보</h4>
            <label for="name">회사명</label>
            <input type="text" id="name" v-model="companyData.name" /><br />
  
            <label for="address">주소</label>
            <input type="text" id="address" v-model="companyData.address" /><br />
  
            <label for="info">매장 소개</label>
            <input type="text" id="info" v-model="companyData.info" /><br />
  
            <label for="phoneNumber">전화번호</label>
            <input
              type="text"
              id="phoneNumber"
              v-model="companyData.phoneNumber"
            />
            <br />
  
            <label for="store">매장 사진</label>
            <input
              type="file"
              multiple
              name="storeFile"
              id="store"
              accept="image/*"
              @change="centerFileChange"
            /><br />
  
            <button type="submit" @click="centerTotalSubmit">보내기</button>
          </form>  
          <button @click="showToast">Toast 확인</button>
          <button @click="closeModal">창닫기</button>      
        </div>
      </div>
  </transition>
</template>

<script setup>
import {ref, defineProps, defineEmits} from 'vue';
import axios from 'axios';
import Toast from '../ui/Toast.vue';
//toast 로직
let message = "";
const isToastVisible = ref(false);

let timeOut = null;
const showToast = ()=> {
    isToastVisible.value = true;
    
    timeOut = setTimeout(()=>{
        isToastVisible.value = false;
        clearTimeout(timeOut);
    }, 1300);
}


//url
const url = "http://localhost:8090/";

//Modal props 설정
const props = defineProps({
  centerRegisterModal: Boolean,
});

const emit = defineEmits(["closeCenterRegisterModal", "checkMessage"]);

const closeModal = () => {
    emit("closeCenterRegisterModal");
}

checkMessage("ㅇㅇㅇㅇ");

const checkMessage = (message) => {
  emit("checkMessage", message)
}

//centerData설정
const companyData = ref({
  name: "",
  address: "",
  phoneNumber: "",
  info: "",
});

const centerImg = ref([]);

const centerFileChange = (e) => {
  const files = e.target.files;
  for (let i = 0; i < files.length; i++) {
    centerImg.value.push(files[i]);
  }
};

const centerTotalSubmit = async (e) => {
  e.preventDefault();

  const centerRigiData = new FormData();

  for (let i = 0; i < centerImg.value.length; i++) {
    centerRigiData.append("centerImg", centerImg.value[i]);
  }

  centerRigiData.append("name", companyData.value.name);
  centerRigiData.append("address", companyData.value.address);
  centerRigiData.append("phoneNumber", companyData.value.phoneNumber);
  centerRigiData.append("info", companyData.value.info);

  try {
    await axios.post(url + "center/infoInsert", centerRigiData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    console.log("야호 성공!");

    //성공 이후 로직
    //초기화
    companyData.value.name = "";
    companyData.value.address = "";
    companyData.value.phoneNumber = "";
    companyData.value.info = "";
    //이미지 처리 초기화
    centerImg.value = [];
    message = "센터 등록 완료";
    showToast();
    message = "";
    closeModal();
  } catch (error) {
    console.log("다시와라 애송이");
    console.log(error);
    message = "센터 등록 실패";
    showToast();
    message = "";
  }
};
</script>

<style scoped>
.black-bg {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 30px;
  padding: 20px;
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
</style>