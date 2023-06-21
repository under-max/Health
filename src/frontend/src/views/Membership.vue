<template>

  <div class="container">

    <div class="d-flex justify-content-center">
      <div class="row top-padding">

        <h4>
          <span class="text-light">센터 검색</span>
        </h4>

        <br>
        <br>

        <!-- 검색 기능 -->
        <div class="d-flex justify-content-start">
          <nav class="navbar bg-body-tertiary">
            <div class="container-fluid">
              <select class="form-select me-2" style="width: 100px;" v-model="searchType">
                <option value="address">지역</option>
                <option value="center">센터</option>
              </select>

              <div class="d-flex">
                <input class="form-control me-2" type="search" placeholder="센터 / 지역 검색" aria-label="Search"
                       v-model="searchKeyword" @keyup.enter="searchConditionEnter"/>

                <button class="btn btn-success" type="button" style="width: 100px;" @click="searchConditionBtn">
                  검색
                </button>

              </div>
            </div>
          </nav>
        </div>

        <br>
        <br>

        <div class="d-flex justify-content-between row">

          <div class="col mt-3">

            <div class="d-flex">

              <h4 class="m-2">
                <span class="text-light">이용권 선택</span>
              </h4>

              <div>
                <button class="btn btn-primary m-1" type="button" @click="getCenterInfo" data-bs-toggle="modal"
                        data-bs-target="#showSelectCenterInfoModal" :disabled="isCenterInfoButtonDisabled">
                  선택한 센터 정보
                </button>
              </div>

              <div>
                <button class="btn btn-primary m-1" type="button" @click="getTrainerInfo" data-bs-toggle="modal"
                        data-bs-target="#showSelectTrainerInfoModal" :disabled="isTrainerInfoButtonDisabled">
                  선택한 트레이너 정보
                </button>
              </div>

            </div>

            <br>

            <div>
              <select class="select--" v-model="selectedCenter" @change="getTrainers">
                <option value="">센터를 선택해주세요.</option>
                <option v-for="center in centerList" :value="center">
                  {{ center.centerName }}
                </option>
              </select>
            </div>

            <div>
              <select class="select--" v-model="selectedTrainer">
                <option value="">트레이너를 선택해주세요.</option>
                <option v-for="trainer in trainerList" :value="trainer">
                  {{ trainer.trainerName }}
                </option>
              </select>
            </div>

            <div>
              <select class="select--" v-model="selectedMonth" :disabled="isMonthDisabled">
                <option value="">이용 기간을 선택해주세요.</option>
                <option v-for="month in monthSelectList" :value="month.value"
                        :disabled="isMonthOptionDisabled(month)">
                  {{ month.name }}
                </option>
              </select>
            </div>

            <div>
              <select class="select--" v-model="selectedPT" @change="selectPTChange">
                <option value="">PT 횟수를 선택해주세요</option>
                <option v-for="pt in ptSelectList" :value="pt.value">{{ pt.name }}</option>
              </select>
            </div>
          </div>

          <div class="col mt-3">

            <h4>
              <span class="text-light">선택 사항</span>
            </h4>

            <br>

            <div>
              <ul class="list-group mb-4">
                <li class="list-group-item">
                  <div>
                    <small class="text-body-secondary">센터 / 트레이너 (필수선택)</small>
                    <h5 class="my-0">{{ selectedCenter.centerName }} / {{ selectedTrainer.trainerName }}</h5>
                  </div>
                </li>
                <li class="list-group-item">
                  <div>
                    <small class="text-body-secondary">이용 기간</small>
                    <h5 class="my-0" v-if="selectedMonth === ''">0 개월</h5>
                    <h5 class="my-0" v-else>{{ selectedMonth }} 개월</h5>
                  </div>
                </li>
                <li class="list-group-item">
                  <div>
                    <small class="text-body-secondary">PT 횟수</small>
                    <h5 class="my-0" v-if="selectedPT === ''">0 회</h5>
                    <h5 class="my-0" v-else>{{ selectedPT }} 회</h5>
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
            </div>

            <div>
              <button class="btn paymentBtn" type="button" data-bs-toggle="modal" data-bs-target="#showPaymentModal"
                      :disabled="isButtonDisabled">카카오페이 결제하기
                <i class="fa-solid fa-barcode"></i></button>
            </div>

          </div>

        </div>

      </div>
    </div>

  </div>

  <!-- 선택한 센터 정보 modal -->
  <div class="modal fade" id="showSelectCenterInfoModal" tabindex="-1" aria-labelledby="showSelectCenterInfoModalLabel"
       aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" style="max-width: 100%;">
      <div class="modal-content showInfoModal-content">

        <div class="modal-header">
          <h2 class="modal-title" id="showSelectCenterInfoModalLabel">센터 정보</h2>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <h5>센터 이름: {{ centerDetail.name }}</h5>
          <h5>센터 주소: {{ centerDetail.address }}</h5>
          <h5>센터 정보: {{ centerDetail.info }}</h5>
          <div class="d-flex justify-content-between">
            <span><i class="fas fa-angle-left fa-2x" @click="moveLeftImage"></i></span>
            <div>
              <img :src="getImage(slideIndex)" alt="" style="max-width: 100%; max-height: 100%;"/>
            </div>
            <span class="btnCheck"><i class="fas fa-angle-right fa-2x" @click="moveRightImage"></i></span>
          </div>
        </div>

        <div class="modal-footer d-flex justify-content-center">
          <button type="button" class="btn btn-link fs-6 text-decoration-none m-0" data-bs-dismiss="modal">확인</button>
        </div>

      </div>
    </div>
  </div>

  <!-- 선택한 트레이너 정보 modal -->
  <div class="modal fade" id="showSelectTrainerInfoModal" tabindex="-1"
       aria-labelledby="showSelectTrainerInfoModalLabel"
       aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" style="max-width: 100%;">
      <div class="modal-content showInfoModal-content">

        <div class="modal-header">
          <h1 class="modal-title fs-3" id="showSelectTrainerInfoModalLabel">트레이너 정보</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <h5>트레이너 이름: {{ trainerDetail.name }}</h5>
          <h5>트레이너 정보: {{ trainerDetail.info }}</h5>
          <div class="trainerImg">
            <img :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${trainerDetail.centerId}/${trainerDetail.id}/${trainerDetail.fileName}`"/>
          </div>
        </div>

        <div class="modal-footer d-flex justify-content-center">
          <button type="button" class="btn btn-link fs-6 text-decoration-none m-0" data-bs-dismiss="modal">확인</button>
        </div>

      </div>
    </div>
  </div>

  <!-- 카카오페이 결제하기 modal -->
  <div class="modal fade" id="showPaymentModal" tabindex="-1" aria-labelledby="showPaymentModalLabel"
       aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content paymentModal-content">

        <div class="modal-header">
          <h1 class="modal-title fs-3" id="showPaymentModalLabel">결제 정보</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <h6>지점 : {{ selectedCenter.centerName }}</h6>
          <h6>담당 트레이너: {{ selectedTrainer.trainerName }}</h6>
          <h6>이용 기간 : {{ selectedMonth }}개월</h6>
          <h6>PT 횟수 : {{ selectedPT === '' ? 0 : selectedPT }} 회</h6>
          <h6>결제 금액 : {{ formattedTotalPrice }}원 </h6>
        </div>

        <div class="modal-footer d-flex justify-content-center">
          <button type="button" class="btn btn-link fs-6 text-decoration-none m-0 border-end" data-bs-dismiss="modal"
                  @click="confirmPayment">
            <strong>구매</strong></button>
          <button type="button" class="btn btn-link fs-6 text-decoration-none m-0" data-bs-dismiss="modal">취소</button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="payReadyUrl">
    <p>결제 링크:</p>
    <a :href="payReadyUrl" target="_blank"> {{ payReadyUrl }}</a>
  </div>

</template>

<script setup>
import {computed, onMounted, ref, watchEffect} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";
import router from "@/router";

const centerDetail = ref({
  id: '',
  centerId: '',
  name: '',
  info: '',
  fileName: '',
});

const trainerDetail = ref({
  id: '',
  name: '',
  address: '',
  info: '',
  fileName: [],
});

const isCenterInfoButtonDisabled = computed(() => {
  return selectedCenter.value === '';
});

const isTrainerInfoButtonDisabled = computed(() => {
  return selectedTrainer.value === '';
});

//이미지 슬라이드 처리
const slideIndex = ref(0);
const getImage = (index) => {
  const fileName = centerDetail.value.fileName[index];
  return `https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerDetail.value.id}/${fileName}`
}
const moveLeftImage = () => {
  if (slideIndex.value > 0) {
    slideIndex.value = slideIndex.value - 1;
  } else {
    slideIndex.value = 0;
  }
}
const moveRightImage = () => {
  if (slideIndex.value < centerDetail.value.fileName.length - 1) {
    slideIndex.value = slideIndex.value + 1;
  } else {
    slideIndex.value = +(centerDetail.value.fileName.length - 1);
  }
}

const isCenterInfo = ref(false);
const isTrainerInfo = ref(false);

const getCenterInfo = () => {
  isCenterInfo.value = true;

  const centerId = selectedCenter.value.centerId;

  axios
      .get(`/api/center/${centerId}`, {
        params: {
          centerId: centerId
        }
      })
      .then((response) => {
        centerDetail.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
};

const getTrainerInfo = () => {
  isTrainerInfo.value = true;

  const trainerId = selectedTrainer.value.trainerId;

  axios
      .get('/api/center/getTrainerDetail', {
        params: {
          trainerId: trainerId
        }
      })
      .then((response) => {
        trainerDetail.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
};

// == 위 코드는 선택한 센터, 트레이너 정보 불러오기 윤석이형 코드 참조 ==

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
const trainerList = ref([]);

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
  {name: "10회", value: 10, price: 300000},
  {name: "20회", value: 20, price: 600000},
  {name: "30회", value: 30, price: 800000},
  {name: "60회", value: 60, price: 1000000}
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

const getCenterList = () => {
  axios
      .get(`/api/membership/centers?type=${searchType.value}&keyword=${searchKeyword.value}`, {})
      .then((response) => {
        if (response.data.length === 0) {
          showCustomAlert("검색 결과가 없습니다.");
        }
        centerList.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
}

// 센터 검색 버튼
const searchConditionBtn = () => {
  getCenterList();
}

// 검색 후 엔터 기능
const searchConditionEnter = () => {
  getCenterList();
};

// 선택 조건 확인하여 결제하기 버튼 활성화 기본값 true
const isButtonDisabled = ref(true);

const checkCondition = () => {
  // 조건을 확인하고 버튼의 활성화 상태를 업데이트
  if ((selectedCenter.value && selectedTrainer.value && selectedMonth.value) || (selectedCenter.value && selectedTrainer.value && selectedPT.value)) {
    isButtonDisabled.value = false; // 버튼 활성화
  } else {
    isButtonDisabled.value = true; // 버튼 비활성화
  }
};

// 변경감지?
watchEffect(() => {
  checkCondition();
});

// modal 창에서 구매버튼
const confirmPayment = () => {
  const {centerId, centerName} = selectedCenter.value;
  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .post("/api/kakaopay", {
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
          window.location.href = response.data
        })
        .catch((error) => {
          alert(error.response.data.message);
        });
    // SuccessPage.vue에서 사용할 데이터 저장
    sessionStorage.setItem("centerId", selectedCenter.value.centerId);
    // 트레이너를 선택하지 않았을 경우 undefined 가 아닌 0이 넘어간다.
    sessionStorage.setItem("trainerId", (selectedTrainer.value.trainerId ? selectedTrainer.value.trainerId : 0));
  } else {
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  }
};

// 센터를 선택하면 소속되어 있는 트레이너 불러오는 axios
const getTrainers = () => {
  axios
      .get(`/api/membership/centers/${selectedCenter.value.centerId}`, {})
      .then((response) => {
        if (response.data.length === 0) {
          showCustomAlert("등록되어 있는 트레이너가 없습니다.");
        }
        trainerList.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
};

// 등록되어있는 센터 가져오기 (url에서 값 제거 후 config에 params 사용 가능)
onMounted(() => {
  axios
      .get(`/api/membership/centers?type=${searchType.value}&keyword=${searchKeyword.value}`, {})
      .then((response) => {
        centerList.value = response.data;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
});

</script>

<style scoped>

.select-- {
  padding: 2vh;
  margin-bottom: 0.1vh;
  border-radius: 5px;
  width: 500px;
}

select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25);
}

.list-group-item {
  width: 500px;
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

.modal {
  padding: 8rem;

}

.showInfoModal-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 500px;
}

.paymentModal-content {
  width: 400px;
}

.modal-footer {
  gap: 1rem;
}

.modal-footer button {
  flex-grow: 1;
}

.top-padding {
  padding-top: 15vh;
}

.trainerImg{

}

.trainerImg img {
  max-height: 40vh;
  max-width: 100%;
  margin-left: auto;
  margin-right: auto;
}

.paymentBtn {
  background-color: yellow;
}

</style>