<template>
  
  <Toast></Toast>
  <!-- 매장 등록 컴포넌트 -->
  <Register v-if="centerRegisterModal" :centerRegisterModal="centerRegisterModal" @closeCenterRegisterModal="closeCenterRegisterModal"></Register>
  <button @click="centerRegisterModal = true">매장등록컴포넌트</button>  
 

  <!-- 매장 정보 수정 컴포넌트 -->
  <Modify v-if="centerModifyModal" :centerModifyModal="centerModifyModal" @closeCenterModifyModal="closeCenterModifyModal" @checkMessage="checkMessage"></Modify>
  <button @click="centerModifyModal = true">매장수정컴포넌트</button>


  <h3>{{message}}ㅁㅁㅁㅁㅁㅁㅁㅁ</h3>
  <!-- center 수정하기 추후 컴포넌트화 예정 css 아직-->
  <transition name="modifyCenter">
    <div class="modify-black" v-if="CenterModify">
      <div class="modify-white">
        <h1>center 수정</h1>

        <!-- 센터검색창 -->
        <input v-model="centerName" />
        <button @click="centerSearchBtn">센터찾기</button>
        <select name="" id="" v-model="centerSelect">
          <option
            v-for="(centerData, index) in centerGetData"
            :value="centerData.id"
            :key="index"
          >
            {{ centerData.name }}
          </option>
        </select>

        <h2>{{ centerSelect }}</h2>

        <div v-if="dataSuccess && centerGetData !== null">
          <h1>aa</h1>
          <div v-for="(centerData, index) in centerGetData" :key="index">
            <div v-if="centerData.id === centerSelect">

              <h1>{{ index }}</h1>
              <h1>ID : {{ centerData.id }}</h1>
              <h1>매장명: {{ centerData.name }}</h1>
              <h1>주소: {{ centerData.address }}</h1>
              <h1>정보: {{ centerData.info }}</h1>
              <h1>전화번호 {{ centerData.phoneNumber }}</h1>

              <div v-for="(imgData, index) in centerData.fileName" :key="index">
                <div v-if="imgData != null">
                  <h1>사진</h1>
                  <img
                    :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerData.id}/${imgData}`"
                  />
                </div>
              </div>
              <hr />
            </div>
          </div>
        </div>

        <p>aaaa</p>

        <button @click="CenterModify = false">나가기</button>
      </div>
    </div>
  </transition>

  <!-- 매장검색 창 추후 components화 예정-->
  <div class="search-container">
    <h1>서치창</h1>
    
    <button @click="CenterModify = true">매장정보 수정</button>
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
</template>

<script setup>
import { ref } from "vue";

import Register from "../components/center/container/Register.vue";
import Modify from "../components/center/container/Modify.vue";
import Toast from "../components/center/ui/Toast.vue";
import axios from "axios";

//잠깐 양방향 props 확인중
const message = ref('');

const checkMessage = (message) => {
  message.value = message;
}



const url = "http://localhost:8090/";


// 확인 끝 Register Modal 로직
const centerRegisterModal = ref(false);

const closeCenterRegisterModal = () => {
  centerRegisterModal.value = false;
}

// Modify Modal 로직 처리중
const centerModifyModal = ref(false);

const closeCenterModifyModal = () => {
  centerModifyModal.value = false;
}






//몇개 찾았는지
const centerSelect = ref(0);
//centerModify modal
const CenterModify = ref(false);

//centerSearch
const centerName = ref("");

//centerSearch axios and data
const centerGetData = ref([]);

//center show data
const dataSuccess = ref(false);

const centerSearchBtn = async () => {
  try {
    const response = await axios.get(url + "center/searchname", {
      params: {
        centerName: centerName.value,
      },
    });
    centerGetData.value = response.data;
    console.log(centerGetData.value);
    console.log(centerGetData.value[0].fileName);
    dataSuccess.value = true;
  } catch (error) {
    console.log(error);
    dataSuccess.value = false;
  }
};



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

//get구현
// const getData = ref(null);

// watchEffect(async () => {
//   const response = await axios.get("/search");
//   getData.value = await response.data;
//   console.log(getData.value);
// })

//delete 구현
</script>

<style scoped>
h1 {
  color: black;
}

div {
  box-sizing: border-box;
}

.modify-black {
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;

  overflow: hidden;
}

.modify-white {
  width: 100%;
  height: auto;
  background: white;
  border-radius: 30px;
  padding: 20px;

  max-height: 90vh;
  overflow-y: auto;
}


.modifyCenter-enter-from {
  opacity: 0;
}
.modifyCenter-enter-active {
  transition: all 0.8s;
}

.modifyCenter-enter-to {
  opacity: 1;
}
.search-container {
  margin-left: 8vh;
  margin-right: 8vh;
  width: auto;
  height: auto;
  min-height: 100vh;
}
</style>