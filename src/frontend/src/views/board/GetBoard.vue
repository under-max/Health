<template>

  <div class="container-lg">

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" :value="board.title" readonly/>
    </div>

    <div class="d-flex justify-content-sm-start">
      <div class="mb-3">
        <label for="" class="form-label">작성자</label>
        <input type="text" class="form-control" :value="board.writer" readonly/>
      </div>

      <div class="mb-3">
        <label for="" class="form-label">작성일시</label>
        <input type="text" readonly class="form-control" :value="board.inserted"/>
      </div>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" readonly rows="10">{{board.content}}</textarea>
    </div>

    <div class="justify-content-end">
      <div class="mb-3">
        <button type="button" class="btn btn-primary" @click="likeUpBtn"><i class="fa-solid fa-chevron-up"></i>
        </button>
        <button type="button" class="btn btn-light">{{ board.likeCount }}</button>
        <button type="button" class="btn btn-danger" @click="likeDownBtn"><i class="fa-solid fa-chevron-down"></i>
        </button>
      </div>
    </div>

    <div v-if="showLikeModal" class="modal-overlay">
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

  </div>

</template>

<script setup>

import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import router from "@/router";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  },
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  likeCount: '',
  inserted: ''
})

// 추천 업 버튼
const likeUpBtn = () => {
  const token = Cookies.get('accessToken');

  if (Cookies.get('accessToken')) {
    axios
        .patch(`/api/community/board/${props.boardId}/likeUp`, {}, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          board.value.likeCount = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
  } else {
    const ok = confirm("로그인이 필요합니다.");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  }
}

// 추천 다운 버튼
const likeDownBtn = () => {
  const token = Cookies.get('accessToken');

  if (Cookies.get('accessToken')) {
    axios
        .patch(`/api/community/board/${props.boardId}/likeDown`, {}, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          board.value.likeCount = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
  } else {
    const ok = confirm("로그인이 필요합니다.");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  }
};

onMounted(() => {
  axios
      .get(`/api/community/board/${props.boardId}`, {})
      .then((response) => {
        console.log(response.data);
        board.value = response.data;
      })
      .catch((error) => {
        console.log(error)
        router.replace("/community")
        alert(error.response.data.message);
      });
});
</script>

<style scoped>

.container-lg {
  margin-top: 100px;
}

</style>