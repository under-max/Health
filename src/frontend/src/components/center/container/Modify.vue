<template>
  <transition name="modifyCenter">
    <div class="modify-black">
      <div class="modify-white">
        <h1>center 수정</h1>

        <h2>{{props.centerModifyModal}}</h2>

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

        <button @click="closeModal">나가기</button>
      </div>
    </div>
  </transition>

</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import axios from 'axios';

//url
const url = "http://localhost:8090/";

//props 처리
const props = defineProps({
    centerModifyModal: Boolean,
});

const emit = defineEmits(["closeCenterModifyModal"]);

const closeModal = () => {
    emit("closeCenterModifyModal");
}

//그냥 아이디 확인용
const centerSelect = ref(0);
//찾을 센터 이름
const centerName = ref("");

//검색 완료 후 값 담을 변수
const centerGetData = ref([]);

//모달창 용인데 흠
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
</script>

<style scoped>
/* //현재 전역으로 지정되어있어서 h1 글자색 바꿈  */
h1{
    color: black;
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
</style>