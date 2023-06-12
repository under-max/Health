<template>

  <div class="container">

    <div class="d-flex justify-content-center">
      <div class="row top-padding">
        <div class="col-md-6">
          <div>
            <div>

              <!-- 검색 기능 -->
              <h4>
                <span class="text-light">센터 검색</span>
              </h4>

              <div>
                <select v-model="searchType" style="width: 100px; height: 30px; padding: 0px">
                  <option value="address">지역</option>
                  <option value="center">센터</option>
                </select>
                <input id="search" type="text" v-model="searchKeyword"/>

                <button class="btn btn-secondary" @click="searchConditionBtn">
                  검색
                </button>
              </div>

              <h4>
                <span class="text-light">이용권 선택</span>
              </h4>

              <div>

                <div>
                  <select v-model="selectedCenter" @change="getTrainers">
                    <option value="">센터를 선택해주세요.</option>
                    <option v-for="center in centerList" :value="center">
                      {{ center.centerName }}
                    </option>
                  </select>
                </div>

                <div>
                  <select v-model="selectedTrainer" v-if="trainerList.length !== 0">
                    <option value="">트레이너를 선택해주세요.</option>
                    <option v-for="trainer in trainerList" :value="trainer">
                      {{ trainer.trainerName }}
                    </option>
                  </select>
                  <select v-else>
                    <option value="">등록되어 있는 트레이너가 없습니다.</option>
                  </select>
                </div>

                <div>
                  <select v-model="selectedMonth" :disabled="isMonthDisabled">
                    <option value="">이용 기간을 선택해주세요.</option>
                    <option v-for="month in monthSelectList" :value="month.value"
                            :disabled="isMonthOptionDisabled(month)">
                      {{ month.name }}
                    </option>
                  </select>
                </div>

                <div>
                  <select v-model="selectedPT" @change="selectPTChange">
                    <option value="">PT 횟수를 선택해주세요</option>
                    <option v-for="pt in ptSelectList" :value="pt.value">{{ pt.name }}</option>
                  </select>
                </div>

              </div>

              <div v-if="showPaymentModal" class="modal-overlay">
                <div class="modal modal-sheet position-static d-block bg-body-secondary p-4 py-md-5"
                     tabindex="-1" role="dialog" id="modalChoice">
                  <div class="modal-dialog" role="document">
                    <div class="modal-content rounded-3 shadow">
                      <div class="modal-body p-4 text-center">
                        <p class="mb-0">결제 정보</p>
                        <p class="mb-0">지점 : {{ selectedCenter.centerName }}</p>
                        <p class="mb-0">담당 트레이너: {{ selectedTrainer.trainerName }}</p>
                        <p class="mb-0">이용 기간 : {{ selectedMonth }}개월</p>
                        <p class="mb-0">PT 횟수 : {{ selectedPT === '' ? 0 : selectedPT }}회</p>
                        <p class="mb-0">결제 금액 : {{ formattedTotalPrice }}원 </p>
                      </div>
                      <div class="modal-footer flex-nowrap p-0">
                        <button type="button"
                                class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0 border-end"
                                @click="confirmPayment"><strong>구매</strong></button>
                        <button type="button"
                                class="btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0"
                                data-bs-dismiss="modal" @click="cancelPayment">취소
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
            <h4>
              <span class="text-light">선택 사항</span>
            </h4>
            <ul class="list-group mb-3">
              <li class="list-group-item">
                <div>
                  <small class="text-body-secondary">센터 / 트레이너</small>
                  <h5 class="my-0">{{ selectedCenter.centerName }} / {{ selectedTrainer.trainerName }}</h5>
                </div>
              </li>
              <li class="list-group-item">
                <div>
                  <small class="text-body-secondary">이용 기간</small>
                  <h5 class="my-0">{{ selectedMonth }}개월</h5>
                </div>
              </li>
              <li class="list-group-item">
                <div>
                  <small class="text-body-secondary">PT 횟수</small>
                  <h5 class="my-0">{{ selectedPT }}회</h5>
                </div>
              </li>
              <li class="list-group-item d-flex justify-content-between">
                <div>
                  <h5 class="my-0">결제 금액</h5>
                </div>
                <strong>
                  {{ formattedTotalPrice }}원
                </strong>
              </li>
            </ul>

            <div>
              <form @submit.prevent="submitPayment">
                <button class="btn btn-primary" type="submit" :disabled="isButtonDisabled">카카오페이 결제 <i
                    class="fa-solid fa-barcode"></i></button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>

</template>

<script setup>
import {computed, onMounted, ref, watchEffect} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import {RouterLink} from "vue-router";

// 에러 관련
const errorMessage = ref('');

// 선택한 센터
const selectedCenter = ref('');

// 선택한 트레이너
const selectedTrainer = ref('');

// 선택한 이용 기간
const selectedMonth = ref('');

// 선택한 PT 횟수
const selectedPT = ref('');

const payReadyUrl = ref('');

// 센터 리스트
const centerList = ref([]);

// 센터에 소속된 트레이너 리스트
const trainerList = ref(['']);

// 검색 키워드 관련
const searchType = ref('address');
const searchKeyword = ref('');

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

// PT 횟수 선택시 이용 기간 disabled 기능
const isMonthDisabled = computed(() => {
  return [10, 20, 30, 60].includes(selectedPT.value); // 선택한 값이 10, 20, 30, 60 중 하나인지 확인
});
const isMonthOptionDisabled = ((month) => {
  return isMonthDisabled.value && month.value !== selectedMonth.value;
});

// PT횟수 선택시 이용기간 개월 수 변경
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

// 센터 검색 버튼
const searchConditionBtn = () => {
  axios
      .get(`/api/membership/centers?type=${searchType.value}&keyword=${searchKeyword.value}`, {})
      .then((response) => {
        console.log(response.data);
        centerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
}

// 선택 조건 확인하여 결제하기 버튼 활성화
const isButtonDisabled = ref(true);

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

// 결제버튼 눌렀을 경우 modal 창 보여주기 초기값
const showPaymentModal = ref(false);

// 결제버튼 눌렀을 시 modal 창 실행되는 메서드
const submitPayment = () => {
  console.log(selectedPT.value)
  showPaymentModal.value = true;
}

// modal 창에서 취소버튼
const cancelPayment = () => {
  showPaymentModal.value = false;
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
        alert(error.response.data.message);
      });
  // SuccessPage.vue에서 사용할 데이터 저장
  sessionStorage.setItem("centerId", selectedCenter.value.centerId);
  // 트레이너를 선택하지 않았을 경우 undefined 가 아닌 0이 넘어간다.
  sessionStorage.setItem("trainerId", (selectedTrainer.value.trainerId ? selectedTrainer.value.trainerId : 0));
};

// 센터를 선택하면 소속되어 있는 트레이너 불러오는 axios
const getTrainers = () => {
  axios
      .get(`/api/membership/centers/${selectedCenter.value.centerId}`, {})
      .then((response) => {
        if (response.data.size == 0) {
          selectedTrainer.value = '등록되어 있는 트레이너가 없습니다.';
        }
        trainerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
};

// 등록되어있는 센터 가져오기
onMounted(() => {
  axios
      .get(`/api/membership/centers?keyword=${searchKeyword.value}&type=${searchType.value}`, {})
      .then((response) => {
        centerList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});

</script>

<style scoped>

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

.top-padding {
  padding-top: 20vh;
}

</style>