<script setup>
import {onMounted, ref} from "vue";
import axios from 'axios';
import router from "@/router";

// 결제 성공 시 넘겨받는 데이터
const successData = ref({
  memberId: '',
  itemName: '',
  paymentMonths: '',
  remainingPT: '',
  totalPrice: '',
  approvedAt: ''
});

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
        <h5 class="card-title">{{ successData.itemName }}</h5>
        <p class="card-text">
          <strong>상세내역:</strong> 헬스장 이용권 ({{ successData.paymentMonths }}개월), PT {{ successData.remainingPT }}회
        </p>
        <p class="card-text">
          <strong>가격:</strong> {{ successData.totalPrice }}원
        </p>
      </div>
    </div>

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
  background-color: #ffffff;
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
}

.buttons {
  display: flex;
  gap: 10px;
}
</style>