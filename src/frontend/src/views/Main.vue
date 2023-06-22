<template>

  <main class="hero">
    <section class="main_text">
      <h1>당신의 노력을 서포트 <br>합니다</h1>
      <p>특별한 당신을 위해서 전문적 지식을 갖춘 트레이너를 통해<br>
        당신만을 위한 차별화된 맞춤형 운동, 식단 그날그날의<br>
        운동정보를 실시간으로 확인하세요</p>
      <button>문의하기</button>
    </section>
    <section class="main_image">
      <img :src="Main_Image2" alt="메인이미지">
    </section>
    <p>main 2번째 페이지랑 footer 부분은 반응형 정도는 나중에 기능 개발 하면
      <br> 중점되는 부분 2번쨰 페이지 혹은 공지사항 그냥 2번쨰에 넣고
      footer는 그냥 주저리 부분이라 시간날떄 채우도록 하겠음
    </p>
  </main>


  <div class="main_popup" v-bind:class="{ on: showPopup }">
    <div class="layer_cont">
      <div class="img_wrap">
        안내사항 & 광고1
      </div>
      <div class="btn_wrap">
        <button class="btn_close" @click="hidePopup"></button>
        <button type="button" class="btn_today_close">
          <span>
            <input type="checkbox" v-model="popupCheckBox">오늘 하루 그만 보기
          </span>
        </button>
      </div>
    </div>
  </div>

  <div v-if="isRemainingPT()">
    <div class="position-fixed bottom-0 end-0">
      <br>
      <br>
      <div class="toast" ref="mainToast" style="width: 200px" role="alert" aria-live="polite"
           aria-atomic="true" data-bs-delay="10000">
        <div class="toast-header">
          <strong class="me-auto text-danger">알림</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close">
            <i class="fa-solid fa-x"></i>
          </button>
        </div>
        <div class="toast-body">
          PT {{ remainingPT }}회 남았습니다.
        </div>
      </div>
    </div>
  </div>

</template>

<script setup lang="js">
import Main_Image2 from '@/assets/M_Image.png';
import Cookies from "vue-cookies";
import {onMounted, ref} from "vue";
import axios from "axios";

const showPopup = ref(true);
const popupCheckBox = ref(false);

// toast 관련 변수
const remainingPT = ref();
const mainToast = ref(null);

const handleStorage = {
  setStorage: function (name, exp) {
    // 체크 한 시간으로부터 24시간 적용
    // let date = new Date();
    // date = date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);

    // 체크 한 시간 상관없이 오늘까지만 적용
    let date = new Date().setHours(23, 59, 59, 999);
    localStorage.setItem(name, date);
  },
  getStorage: function (name) {
    let now = new Date();
    now = now.setTime(now.getTime());
    return parseInt(localStorage.getItem(name)) > now;
  },
};

const toggleMainPopup = () => {
  if (handleStorage.getStorage('today')) {
    showPopup.value = false;
  } else {
    showPopup.value = true;
  }
};

// Hide popup
const hidePopup = () => {
  if (popupCheckBox.value) {
    handleStorage.setStorage('today', 1);
    showPopup.value = false;
  } else {
    showPopup.value = false;
  }
};

const isRemainingPT = () => {
  return (Cookies.get('accessToken')) && (0 <= remainingPT.value && remainingPT.value <= 5);
};

onMounted(() => {
  toggleMainPopup();

  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .get("/api/membership/remainingPT", {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          remainingPT.value = response.data;
        })
        .then(() => {
          const bootstrapToast = new bootstrap.Toast(mainToast.value);
          bootstrapToast.show();
        })
        .catch((error) => {
          alert(error.response.data.message);
        });
  }
});

</script>

<style scoped>
.hero {
  margin: 0px;
  width: auto;
  height: auto;
  min-height: 100vh;
  background-color: #112470;
  background-image: linear-gradient(176deg, #112470 35%, #fff);
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  padding: 0 0 0 60px;
  display: flex;
}

.main_text {
  max-width: 1160px;
  min-width: 400px;
  margin-left: 10vh;
  margin-right: 10vh;
  padding-top: 28vh;
  padding-bottom: 28vh;
}

.main_text > h1 {
  color: white;
  margin-top: 0px;
  margin-bottom: 28px;
  padding-top: 50px;
  font-size: 65px;
  line-height: 70px;
}

.main_text > p {
  color: white;
  margin-bottom: 25px;
  font-size: 23px;
  line-height: 40px;
}


.main_text > button {
  height: 65px;
  text-align: center;
  border-radius: 50px;
  margin-left: 0px;
  padding: 15px 61px;
  font-size: 23px;
  color: white;
  background: rgba(255, 130, 0, 0.9);
  border: none;
}

.main_image {
  max-width: 100%;
  margin-left: 0px;
  margin-right: 0px;
}

.main_image > img {
  height: 100vh;
}

button {
  border: 0;
  background: none;
  cursor: pointer;
}

.main_popup {
  position: fixed;
  z-index: 1005;
  -webkit-box-shadow: 0px 13px 40px -6px #061626;
  box-shadow: 0px 13px 40px -6px #061626;
  top: 150px;
  left: 770px;
  display: none;
}

.main_popup.on {
  display: block;
  background-color: #fff;
}

.main_popup .img_wrap {
  width: 300px;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.main_popup .btn_close {
  width: 32px;
  height: 32px;
  position: absolute;
  top: 17px;
  right: 17px;
  font-size: 0;
  border: 0;
  background: none;
}

.main_popup .btn_close::before {
  content: "";
  width: 2px;
  height: 32px;
  background-color: #333;
  position: absolute;
  top: 0;
  left: 15px;
  transform: rotate(45deg);
}

.main_popup .btn_close::after {
  content: "";
  width: 32px;
  height: 2px;
  background-color: #333;
  position: absolute;
  top: 15px;
  left: 0;
  transform: rotate(45deg);
}

.main_popup .btn_today_close {
  width: 100%;
  height: 50px;
  background-color: #333;
  text-align: center;
  color: #fff;
  font-size: 16px;
  display: block;
}

.main_popup .btn_today_close span {
  display: block;
  line-height: 40px;
  vertical-align: bottom;
  opacity: 0.8;
}

</style>