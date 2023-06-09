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
        <input type="text" class="form-control" :value="board.inserted" readonly/>
      </div>

      <div class="mb-3">
        <label for="" class="form-label">조회수</label>
        <input type="text" class="form-control" :value="board.viewCount" readonly/>
      </div>

      <div class="mb-3">
        <label for="" class="form-label">추천수</label>
        <input type="text" class="form-control" :value="board.likeCount" readonly/>
      </div>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" rows="10" readonly>{{board.content}}</textarea>
    </div>

    <div class="d-flex justify-content-between">
      <div class="mb-3">
        <button type="button" class="btn btn-primary" @click="likeUpBtn"><i class="fa-solid fa-chevron-up"></i>
        </button>
        <button type="button" class="btn btn-light">{{ board.likeCount }}</button>
        <button type="button" class="btn btn-danger" @click="likeDownBtn"><i class="fa-solid fa-chevron-down"></i>
        </button>
      </div>

      <div>
        <button type="button" class="btn btn-secondary" @click="modifyBtn">수정</button>
        <button type="button" class="btn btn-danger" @click="deleteBtn">삭제</button>
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
  }
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  likeCount: '',
  inserted: ''
});

// 추천 버튼 로그인 체크 함수
const loginCheck = (like) => {

};

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
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  }
};

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
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  }
};

const modifyBtn = () => {

};

const deleteBtn = () => {
  const token = Cookies.get('accessToken');

  const ok = confirm("게시글을 삭제하시겠습니까?");

  if (ok) {
    axios
        .delete(`/api/community/board/${props.boardId}`, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          if (response.data) {
            alert(response.data);
            router.replace('/community');
          }
        })
        .catch((error) => {
          if (error.response.data) {
            alert(error.response.data);
            router.push(router.currentRoute.value.fullPath);
          }
        });
  } else {
    router.push(router.currentRoute.value.fullPath);
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