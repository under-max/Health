<script setup>
import {onMounted, ref, watchEffect} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";

// Controller에서 AuthUser 추가하면서 잠시 주석
// const memberId = ref(19);
const totalPrice = ref(0);

// 에러 관련
const errorMessage = ref("");
const isButtonDisabled = ref(true);

// 선택한 센터
const selectedCenter = ref({
  centerId: '',
  centerName: ''
});

// 선택한 트레이너
const selectedTrainer = ref({
  trainerId: '',
  trainerName: ''
});

// 선택한 개월 수
const selectedMonth = ref("");
// 선택한 pt 횟수
const selectedPT = ref("");

const payReadyUrl = ref("");

// 결제버튼 눌렀을 경우 modal 창 보여주기 초기값
const showModal = ref(false);

// 센터 리스트
const centerList = ref([]);

// 센터에 소속된 트레이너 리스트
const trainerList = ref([]);

// 개월 리스트
const monthSelectList = ref([
  {name: "선택해주세요.", value: "", price: 0},
  {name: "1개월", value: 1, price: 50000},
  {name: "3개월", value: 3, price: 150000},
  {name: "6개월", value: 6, price: 300000},
  {name: "12개월", value: 12, price: 600000}
]);

// pt 리스트
const ptSelectList = ref([
  {name: "선택안함", value: "", price: 0},
  {name: "5회", value: 5, price: 200000},
  {name: "10회", value: 10, price: 400000},
  {name: "15회", value: 15, price: 600000},
  {name: "20회", value: 20, price: 800000}
]);

// 결제버튼 눌렀을 시 실행되는 메서드
const submitPayment = () => {
  showModal.value = true;
}

// modal 창에서 구매버튼
const confirmPayment = () => {
  const {centerId, centerName} = selectedCenter.value;
  const token = Cookies.get('accessToken')
  axios
      .post("/api/kakaopay", {
        // memberId: memberId.value,
        centerId: centerId,
        centerName: centerName,
        month: selectedMonth.value,
        pt: selectedPT.value,
        totalPrice: totalPrice.value
      }, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        console.log(response.data);
        window.location.href = response.data
      })
      .catch((error) => {
        console.log(error);
        this.errorMessage = error.response.data.message;
      });
  // SuccessPage.vue에서 사용할 데이터 저장
  sessionStorage.setItem("centerId", selectedCenter.value.centerId);
  sessionStorage.setItem("trainerId", selectedTrainer.value.trainerId);
};

// modal 창에서 취소버튼
const cancelPayment = () => {
  showModal.value = false;
}

// 선택 조건 확인하여 결제하기 버튼 활성화
const checkCondition = () => {
  // 조건을 확인하고 버튼의 활성화 상태를 업데이트
  if (selectedCenter.value && selectedTrainer.value && selectedMonth.value || selectedPT.value) {
    isButtonDisabled.value = false; // 버튼 활성화

  } else {
    isButtonDisabled.value = true; // 버튼 비활성화
  }
};

// 변경감지?
watchEffect(() => {
  checkCondition();
});

// 센터를 선택하면 해당하는 트레이너 불러오는 axios
const fetchTrainers = () => {
  axios
      .get(`/api/membership/centers/${selectedCenter.value.centerId}`, {})
      .then((response) => {
        trainerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
};

// 등록되어있는 센터 가져오기
onMounted(() => {
  axios
      .get("/api/membership/centers", {})
      .then((response) => {
        centerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});
</script>

<template>
  <div class="container">
    <h3>구매</h3>
    <div>

      <div v-if="errorMessage">{{ errorMessage }}</div>

      <div>
        <select v-model="selectedCenter" @change="fetchTrainers">
          <option disabled value="">다음 중 하나를 선택하세요</option>
          <option v-for="center in centerList" :value="center">
            {{ center.centerName }}
          </option>
        </select>
      </div>

      <div>
        <select v-model="selectedTrainer">
          <option value="">선택안함</option>
          <option v-for="trainer in trainerList" :value="trainer">
            {{ trainer.trainerName }}
          </option>
        </select>
      </div>

      <div>
        <select v-model="selectedMonth">
          <option v-for="month in monthSelectList" :value="month.value">{{ month.name }}</option>
        </select>
      </div>

      <div>
        <select v-model="selectedPT">
          <option v-for="pt in ptSelectList" :value="pt.value">{{ pt.name }}</option>
        </select>
      </div>

      {{ selectedCenter.centerName }} <br>

      총 가격 :
      {{
        totalPrice = monthSelectList?.find(item => item.value === selectedMonth).price + ptSelectList?.find(item => item.value === selectedPT).price
      }}원

    </div>

    <div>
      <form @submit.prevent="submitPayment">
        <button class="btn btn-primary" type="submit" :disabled="isButtonDisabled">카카오페이로 결제하기</button>
      </form>
    </div>

    <div v-if="showModal" class="modal-overlay">
      <div class="modal modal-sheet position-static d-block bg-body-secondary p-4 py-md-5"
           tabindex="-1" role="dialog" id="modalChoice">
        <div class="modal-dialog" role="document">
          <div class="modal-content rounded-3 shadow">
            <div class="modal-body p-4 text-center">
              <h5 class="mb-0">구매 결정</h5>
              <p class="mb-0">지점 : {{ selectedCenter.centerName }}</p>
              <p class="mb-0">담당 트레이너: {{ selectedTrainer.trainerName }}</p>
              <p class="mb-0">이용권 : {{ selectedMonth }}개월</p>
              <p class="mb-0">PT횟수 : {{ selectedPT }}회</p>
              <p class="mb-0">구매 가격 : {{ totalPrice }}원 </p>
            </div>
            <div class="modal-footer flex-nowrap p-0">
              <button type="button"
                      class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0 border-end"
                      @click="confirmPayment"><strong>사요.</strong></button>
              <button type="button" class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0"
                      data-bs-dismiss="modal" @click="cancelPayment">안사요.
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="payReadyUrl">
      <h3>결제 링크:</h3>
      <a :href="payReadyUrl" target="_blank"> {{ payReadyUrl }}</a>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.container > div {
  margin-bottom: 1rem;
}

.container form {
  margin-bottom: 1rem;
}

.container p {
  margin-bottom: 0.5rem;
}

select {
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  max-width: 300px;
}

select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25);
}

select option[disabled] {
  color: #999;
}

button.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  color: #fff;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border-radius: 4px;
  cursor: pointer;
}

button.btn-primary:hover {
  background-color: #0069d9;
  border-color: #0062cc;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  width: 400px;
}

.modal {
  border-radius: 12px;
  padding: 2rem;
  height: 500px;
  width: 600px;
}

.modal-overlay,
.modal {
  overflow: auto;
}

.modal-footer {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.modal-footer button {
  flex-grow: 1;
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>