<template>

  <h4>이용권안내!</h4>
  <div class="container">
    <div class="row top-padding">
      <div class="col-md-6">
        <div>
          <div class="container">

            <div>
              <form>
                <h4>
                  <label class="text-primary" for="searchStore">검색</label>
                  <input v-model="searchStore" id="searchStore"/>
                  <button @click="searchStoreSubmit">검색</button>
                </h4>
              </form>
            </div>

            <h4 class="d-flex justify-content-between align-items-center mb-3">
              <span class="text-primary">선택</span>
            </h4>

            <div>

              <div v-if="errorMessage">{{ errorMessage }}</div>

              <div>
                <select v-model="selectedCenter" @change="getTrainers">
                  <option value="">다음 중 하나를 선택하세요</option>
                  <option v-for="center in centerList" :value="center">
                    {{ center.centerName }}
                  </option>
                </select>
              </div>

              <div>
                <select v-model="selectedTrainer">
                  <option value="">다음 중 하나를 선택하세요</option>
                  <option v-for="trainer in trainerList" :value="trainer">
                    {{ trainer.trainerName }}
                  </option>
                </select>
              </div>

              <div>
                <select v-model="selectedMonth">
                  <option value="">다음 중 하나를 선택하세요</option>
                  <option v-for="month in monthSelectList" :value="month.value">
                    {{ month.name }}
                  </option>
                </select>
              </div>

              <div>
                <select v-model="selectedPT" @change="selectPTChange">
                  <option value="">다음 중 하나를 선택하세요</option>
                  <option v-for="pt in ptSelectList" :value="pt.value">{{ pt.name }}</option>
                </select>
              </div>
            </div>


            <div v-if="showModal" class="modal-overlay">
              <div class="modal modal-sheet position-static d-block bg-body-secondary p-4 py-md-5"
                   tabindex="-1" role="dialog" id="modalChoice">
                <div class="modal-dialog" role="document">
                  <div class="modal-content rounded-3 shadow">
                    <div class="modal-body p-4 text-center">
                      <p class="mb-0">구매 결정</p>
                      <p class="mb-0">지점 : {{ selectedCenter.centerName }}</p>
                      <p class="mb-0">담당 트레이너: {{ selectedTrainer.trainerName }}</p>
                      <p class="mb-0">이용 기간 : {{ selectedMonth }}개월</p>
                      <p class="mb-0">PT 횟수 : {{ selectedPT }}회</p>
                      <p class="mb-0">구매 가격 : {{ formattedTotalPrice }}원 </p>
                    </div>
                    <div class="modal-footer flex-nowrap p-0">
                      <button type="button"
                              class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0 border-end"
                              @click="confirmPayment"><strong>사요</strong></button>
                      <button type="button"
                              class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0"
                              data-bs-dismiss="modal" @click="cancelPayment">안사요
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="payReadyUrl">
              <p>결제 링크:</p>
              <a :href="payReadyUrl" target="_blank"> {{ payReadyUrl }}</a>
            </div>
          </div>
        </div>
      </div>


      <div class="col-md-6">
        <div class="row col-lg-8">
          <!--          <div class="col-md-8 col-lg-6 order-md-last">-->
          <h4 class="d-flex justify-content-between align-items-center mb-3">
            <span class="text-primary">선택 사항</span>
          </h4>
          <ul class="list-group mb-3">
            <li class="list-group-item d-flex justify-content-between lh-sm">
              <div>
                <small class="text-body-secondary">센터 / 트레이너</small>
                <h5 class="my-0">{{ selectedCenter.centerName }} / {{ selectedTrainer.trainerName }}</h5>
              </div>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-sm">
              <div>
                <small class="text-body-secondary">이용 기간</small>
                <h5 class="my-0">{{ selectedMonth }}개월</h5>
              </div>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-sm">
              <div>
                <small class="text-body-secondary">PT 횟수</small>
                <h5 class="my-0">{{ selectedPT }}회</h5>
              </div>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-sm">
              <div>
                <h5 class="my-0">합계</h5>
              </div>
              <strong>
                {{ formattedTotalPrice }}원
              </strong>
            </li>
          </ul>

          <div>
            <form @submit.prevent="submitPayment">
              <button class="btn btn-primary" type="submit" :disabled="isButtonDisabled">카카오페이로 결제하기</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>

<script setup>
import {computed, onMounted, ref, watch, watchEffect} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";



// 선택한 센터
const selectedCenter = ref('');
// const selectedCenter = ref({
//   centerId: '',
//   centerName: ''
// });

// 선택한 트레이너
const selectedTrainer = ref('');
// const selectedTrainer = ref({
//   trainerId: '',
//   trainerName: ''
// });

// 선택한 이용 기간
const selectedMonth = ref("");

// 선택한 PT 횟수
const selectedPT = ref("");

// watch(selectedPT,(currval, oldval)=>{
//   console.log(currval);
// })

const payReadyUrl = ref("");

// 센터 리스트
const centerList = ref([]);

// 센터에 소속된 트레이너 리스트
const trainerList = ref([]);

// 이용 기간 리스트
const monthSelectList = ref([
  {name: "선택안함", value: 0, price: 0},
  {name: "1개월", value: 1, price: 50000},
  {name: "2개월", value: 2, price: 100000},
  {name: "3개월", value: 3, price: 130000},
  {name: "6개월", value: 6, price: 220000},
  {name: "12개월", value: 12, price: 400000}
]);

// PT 횟수 리스트
const ptSelectList = ref([
  {name: "선택안함", value: 0, price: 0},
  {name: "10회 / 1개월", value: 10, price: 300000},
  {name: "20회 / 2개월", value: 20, price: 600000},
  {name: "30회 / 3개월", value: 30, price: 800000},
  {name: "60회 / 6개월", value: 60, price: 1000000}
]);

// 이용 기간, PT 횟수 가격 합계
const totalPrice = computed(() => {
  const monthPrice = monthSelectList.value?.find(item => item.value === selectedMonth.value)?.price || 0;
  const ptPrice = ptSelectList.value?.find(item => item.value === selectedPT.value)?.price || 0;
  return monthPrice + ptPrice;
});

// 가격 합계에 쉼표 넣어주는 메서드
const formattedTotalPrice = computed(() => {
  return totalPrice.value.toLocaleString();
});

const selectPTChange = () => {
  if (selectedPT.value == 10) {
    selectedMonth.value = "1";
  } else if (selectedPT.value == 20) {
    selectedMonth.value = "2";
  } else if (selectedPT.value == 30) {
    selectedMonth.value = "3";
  } else if (selectedPT.value == 60) {
    selectedMonth.value = "6";
  }
}

// 에러 관련
const errorMessage = ref("");
const isButtonDisabled = ref(true);


// 결제버튼 눌렀을 경우 modal 창 보여주기 초기값
const showModal = ref(false);

// 결제버튼 눌렀을 시 실행되는 메서드
const submitPayment = () => {
  showModal.value = true;
}

// modal 창에서 취소버튼
const cancelPayment = () => {
  showModal.value = false;
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
  // 트레이너를 선택하지 않았을 경우 undefined 가 아닌 0이 넘어간다.
  sessionStorage.setItem("trainerId", (selectedTrainer.value.trainerId ? selectedTrainer.value.trainerId : 0));
};

// 선택 조건 확인하여 결제하기 버튼 활성화
const checkCondition = () => {
  // 조건을 확인하고 버튼의 활성화 상태를 업데이트
  if ((selectedCenter.value && selectedMonth.value) || (selectedCenter.value && selectedTrainer.value && selectedPT.value)) {
    isButtonDisabled.value = false; // 버튼 활성화
  } else {
    isButtonDisabled.value = true; // 버튼 비활성화
  }
};

// 변경감지?
watchEffect(() => {
  checkCondition();
});

// 센터를 선택하면 소속되어 있는 트레이너 불러오는 axios
const getTrainers = () => {
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
      .get("/api/membership/centers" +
          "", {})
      .then((response) => {
        centerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});

</script>

<style scoped>

.container {
  display: flex;
  flex-direction: column;
  margin-left: 10rem;
//justify-content: center; //align-items: center; height: 100vh;
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
  padding: 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 430px;
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

.top-padding {
  padding-top: 3rem;
}

.row {
  margin-right: -0.5rem;
  margin-left: -0.5rem;
}

.col-md-6 {
  padding-right: 0.5rem;
  padding-left: 0.5rem;
}

</style>