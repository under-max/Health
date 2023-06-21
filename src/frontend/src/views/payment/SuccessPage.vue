<template>

  <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">

        <div class="modal-header">
          <h4 class="modal-title">이용권 결제 상세 내역</h4>
        </div>

        <div class="modal-body">
          <small>환불은 오프라인으로만 가능합니다.</small>&#x1F60B; <br><br>
          <h5>이용 센터 : {{ successData.centerName }} </h5>
          <h5>이용 기간 : {{ successData.paymentMonths }}개월 </h5>
          <h5>PT : {{ successData.remainingPT }}회 </h5>
          <h5>결제 가격 : {{ formattedTotalPrice }}원 </h5>
          <h5>결제 일자 : {{ successData.approvedDate }} </h5>
          <h5>결제 시간 : {{ successData.approvedTime }} </h5>
        </div>

        <div class="modal-footer">
          <button class="btn btn-primary" @click="goToExercise">운동관리 화면으로</button>
          <button class="btn btn-secondary" @click="goToHome">홈 화면으로</button>
        </div>

      </div>
    </div>
  </div>

</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import axios from 'axios';
import router from "@/router";
import Cookies from "vue-cookies";

const showModal = ref(false);

const goToExercise = () => {
  router.replace(`/exercise`);
}

const goToHome = () => {
  router.replace("/");
}

// 가격 합계에 쉼표 넣어주는 메서드
const formattedTotalPrice = computed(() => {
  return successData.value.totalPrice.toLocaleString();
});

// 결제 성공 시 서버에서 받는 데이터
const successData = ref({
  memberId: '',
  centerName: '',
  paymentMonths: '',
  remainingPT: '',
  totalPrice: '',
  approvedDate: '',
  approvedTime: ''
});

// kakaoPay 성공
const urlParams = new URLSearchParams(window.location.search);
const pg_token = urlParams.get('pg_token');

onMounted(() => {
  const token = Cookies.get('accessToken');

  axios
      .get(`/api/kakaopay/${pg_token}`, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        console.log(response.data);
        successData.value = response.data;

        axios
            .post("/api/membership", {
              memberId: successData.value.memberId,
              paymentMonths: successData.value.paymentMonths,
              remainingPT: successData.value.remainingPT,
              // Membership.vue 에서 넘어온 데이터 사용
              centerId: sessionStorage.getItem("centerId"),
              trainerId: sessionStorage.getItem("trainerId")

            }, {
              headers: {
                Authorization: token
              }
            })
            .then((response) => {
            })
            .catch((error) => {
              alert(error.response.data.message);
            });
      })
      .catch((error) => {
        alert(error.response.data.message);
      });

  showModal.value = true;
});

</script>

<style scoped>

.modal-content {
  padding: 5px;
}

</style>