<script setup>
import {computed, inject, onMounted, ref, watchEffect} from "vue";
import axios from 'axios';
import router from "@/router";

// 결제 성공 시 넘겨받는 데이터
const successData = ref({
  memberId: '',
  centerName: '',
  paymentMonths: '',
  remainingPT: '',
  totalPrice: '',
  approvedDate: '',
  approvedTime: ''
});

const check = ()=>{
  console.log(sessionStorage.getItem("centerId"))
  console.log(sessionStorage.getItem("trainerId"))
}
// Insert.vue 에서 넘어온 데이터 사용
const goToMyInfo = () => {
  router.replace("/");
}

const goToHome = () => {
  router.replace("/");
}
// kakaoPay 성공
const urlParams = new URLSearchParams(window.location.search);
const pg_token = urlParams.get('pg_token');

onMounted(() => {
  axios
      .get(`/api/kakaopay/${pg_token}`, {})
      .then((response) => {
        console.log(response.data);
        successData.value = response.data;
        // window.location.href = response.data.url;
        axios
            .post("/api/membership", {
              memberId: successData.value.memberId,
              paymentMonths: successData.value.paymentMonths,
              remainingPT: successData.value.remainingPT
            })
            .then((response) => {
              console.log(response.data);
              console.log(sessionStorage.getItem("centerId"))
              console.log(sessionStorage.getItem("trainerId"))
            })
            .catch((error) => {
              console.log(error)
            });
      })
      .catch((error) => {
        console.log(error)
      });
// router.replace(`/membership/${props.userId}`)

});
</script>

<template>
  <div class="container">
    <div class="card">
      <div class="card-body">
        <h5 class="card-title"><strong>===상세내역===</strong></h5>
        <p class="card-text">
          지점 : {{ successData.centerName }} <br>
          기한 : {{ successData.paymentMonths }}개월 <br>
          pt : {{ successData.remainingPT }}회 <br>
          가격 : {{ successData.totalPrice }}원 <br>
          결제일 : {{ successData.approvedDate}} <br>
          결제시간 : {{ successData.approvedTime }} <br>
        </p>
      </div>
    </div>
    <button @click="check">123123</button>
    <div class="buttons">
      <button class="btn btn-primary" @click="goToMyInfo">내 정보</button>
      <button class="btn btn-secondary" @click="goToHome">홈으로</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 80vh;
  padding: 20px;
}

.card {
  max-width: 500px;
  max-height: 600px;
  margin-bottom: 20px;
}

.card-title {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.card-text {
  margin-bottom: 5px;
  text-align: center;
}

.buttons {
  display: flex;
  gap: 10px;
}
</style>