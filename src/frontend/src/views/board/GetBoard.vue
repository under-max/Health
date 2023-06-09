<template>

  <div class="container-lg">

    <div class="d-flex justify-content-between">

      <div id="info">
        <span>{{ board.writer }}</span>
        <span> / </span>
        <span><i class="fa-solid fa-binoculars"></i> {{ board.viewCount }}</span>
        <span> / </span>
        <span><i class="fa-solid fa-bolt"></i> {{ board.likeCount }}</span>
        <span> / </span>
        <span>{{ board.inserted }}</span>
      </div>

      <div>
        <button type="button" class="btn btn-secondary" @click="modifyBtn">수정</button>
        <button type="button" class="btn btn-danger" @click="deleteBtn">삭제</button>
      </div>

    </div>

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" :value="board.title" readonly/>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" rows="10" readonly>{{board.content}}</textarea>
    </div>

    <div class="d-flex justify-content-end">
      <div class="mb-3">
        <button type="button" class="btn btn-primary" @click="likeUpBtn"><i class="fa-solid fa-chevron-up"></i>
        </button>
        <button type="button" class="btn btn-light">{{ board.likeCount }}</button>
        <button type="button" class="btn btn-danger" @click="likeDownBtn"><i class="fa-solid fa-chevron-down"></i>
        </button>
      </div>

    </div>

    <div class="d-flex border-danger">
      <textarea v-model="commentContent" class="comment-textarea"></textarea>
      <button @click="CommentBtn" class="comment-button">댓글</button>
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
  viewCount: '',
  likeCount: '',
  inserted: ''
});

const commentContent = ref('');

function CommentBtn(event) {
  event.preventDefault(); // 폼 기본 동작 방지

  const token = Cookies.get('accessToken');

  axios
      .post('/api/community/comment', {
        comment: commentContent.value
      }, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
      })
      .catch((error) => {
        console.error(error);
      });
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

// 게시글 수정
const modifyBtn = () => {
  router.push(`/community/board/${props.boardId}/modify`);
};

// 게시글 삭제
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

#info {
  color: white;
}

.comment-textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1px solid #ccc;
}

.comment-button {
  padding: 5px 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
}

</style>