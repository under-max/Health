<template>
  <div class="centerRegister">
    <div class="black-bg">
      <h4>매장 등록</h4>
      <div class="white-bg">
        <form action="" enctype="multipart/form-data">
          <label for="name">회사명</label>
          <input type="text" id="name" v-model="companyData.name" /><br />

          <label for="address">주소</label>
          <input type="text" id="address" v-model="companyData.address" /><br />

          <label for="info">매장 소개</label>
          <input
            type="text"
            id="info"
            v-model="companyData.info"
            class="centerInfo"
          /><br />

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

          <button type="submit" @click="centerTotalSubmit">등록하기</button>
        </form>

        <button class="centerModifyBtn">수정하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, defineProps, defineEmits} from 'vue';
import axios from 'axios';


//url
const url = "http://localhost:8090/";

//Modal props 설정
const props = defineProps({
  centerRegisterModal: Boolean,
  modelValue: String,
});

const emit = defineEmits(["closeCenterRegisterModal", "modelValue"]);

const closeModal = () => {
    emit("closeCenterRegisterModal");
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

    //성공 이후 로직
    //초기화
    companyData.value.name = "";
    companyData.value.address = "";
    companyData.value.phoneNumber = "";
    companyData.value.info = "";
    //이미지 처리 초기화
    centerImg.value = [];     
      
  } catch (error) {    
    console.log(error);   
  }
};
</script>

<style scoped>
h1 {
  color: black;
}

.black-bg {
  background-color: #f5f5f5;
  padding: 20px;
  color: #fff;
}

.white-bg {
  background-color: #fff;
  padding: 20px;
}

form {
  display: flex;
  flex-direction: column;
}

form label {
  margin-bottom: 5px;
}

form input[type="text"] {
  margin-bottom: 10px;
  padding: 5px;
}

form input[type="file"] {
  margin-bottom: 10px;
}

form button[type="submit"] {
  background-color: #3850d2;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

form button[type="submit"]:hover {
  background-color: #112470;
}

button {
  margin-top: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

form label {
  margin-bottom: 5px;
  display: block;
  color: #3333339b;
  font-weight: bold;
}

.black-bg h4 {
  color: black;
  text-align: center;
}

.centerModifyBtn {
  background-color: yellow;
  color: black;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
}

.centerModifyBtn:hover {
  background-color: #ffffcc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.centerInfo {
  height: 12vh;
}
</style>