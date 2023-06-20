<template>

  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>

  <div v-else>

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
          <div v-if="currentUser === boardWriter">
            <button type="button" class="btn btn-secondary" @click="modifyBtn">수정</button>
            <button type="button" class="btn btn-danger" @click="deleteBtn">삭제</button>
            <button class="btn btn-warning" type="button" @click="goListBtn">목록</button>
          </div>

          <div v-else>
            <button class="btn btn-warning" type="button" @click="goListBtn">목록</button>
          </div>
        </div>

      </div>

      <div class="mb-3">
        <label for="" class="form-label">제목</label>
        <input type="text" class="form-control" :value="board.title" readonly/>
      </div>

      <div class="mb-3">
        <label for="" class="form-label">내용</label>
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

      <div v-if="Cookies.get('accessToken')">
        <div class="d-flex border-danger">
          <textarea v-model="addCommentContent" class="comment-textarea" placeholder="댓글을 입력해주세요."></textarea>
          <button @click="addCommentBtn" class="comment-button">댓글 쓰기</button>
        </div>
      </div>

      <div v-else>
        <div class="d-flex border-danger">
          <div class="comment-div">
            댓글을 쓰려면
            <router-link to="/login">로그인</router-link>
            이 필요합니다.
          </div>
          <button class="comment-button" disabled>댓글 쓰기</button>
        </div>
      </div>

      <br>

      <div class="card">
        <div class="card-header">댓글 리스트</div>
        <ul class="list-group" v-for="comment in commentList" :key="comment.id">
          <li class="list-group-item d-flex justify-content-between">
            <div>{{ comment.content }}</div>
            <div class="d-flex">
              <div>{{ comment.writer }} /&nbsp;</div>
              <div>{{ comment.inserted }} &nbsp;</div>
              <div v-if="currentUser === comment.writer">
                <button type="button" @click="assignComment(comment.id, comment.content)"
                        class="badge btn btn-secondary"
                        data-bs-toggle="modal" data-bs-target="#myModal">수정
                </button>
                <button type="button" @click="deleteCommentBtn(comment.id)" class="badge btn btn-danger">삭제</button>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <!-- 수정 modal -->
      <div class="modal fade" id="myModal">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">

            <div class="modal-header">
              <h4 class="modal-title">댓글 수정</h4>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
              <textarea v-model="modifyCommentContent" class="comment-textarea"></textarea>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-danger" data-bs-dismiss="modal">취소</button>
              <button type="button" @click="modifyCommentBtn" class="btn btn-secondary" data-bs-dismiss="modal">수정
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
import {showCustomAlert} from "@/main";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  }
});

// 게시글 정보
const board = ref({
  title: '',
  content: '',
  writer: '',
  viewCount: '',
  likeCount: '',
  inserted: ''
});

const isLoading = ref(true);

// 댓글 리스트
const commentList = ref([]);

// 등록, 수정 댓글
const addCommentContent = ref('');
const modifyCommentContent = ref('');

const commentId = ref('');

// 현재 로그인한 사용자의 닉네임
const currentUser = ref('guest');

// 게시글 작성자
const boardWriter = ref('');

// 할당 변수
const assignComment = (id, content) => {
  commentId.value = id;
  modifyCommentContent.value = content;
};

// 댓글 등록
const addCommentBtn = () => {
  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .post(`/api/community/board/${props.boardId}/comment`, {
          content: addCommentContent.value
        }, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          showCustomAlert(response.data);
          reloadComment();
          addCommentContent.value = "";
        })
        .catch((error) => {
          alert(error.response.data.errors[0].defaultMessage);
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

// 댓글 수정
const modifyCommentBtn = () => {
  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .put(`/api/community/board/${props.boardId}/comment`, {
          commentId: commentId.value,
          content: modifyCommentContent.value
        }, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          showCustomAlert(response.data);
          reloadComment();
        })
        .catch((error) => {
          alert(error.response.data.errors[0].defaultMessage);
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

// 댓글 삭제
const deleteCommentBtn = (commentId) => {
  const token = Cookies.get('accessToken');

  const ok = confirm("댓글을 삭제하시겠습니까?");

  if (ok && token) {
    axios
        .delete(`/api/community/board/${props.boardId}/comment/${commentId}`, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          showCustomAlert(response.data);
          reloadComment();
        })
        .catch((error) => {
          alert(error.response.data);
          router.push(router.currentRoute.value.fullPath);
        });
  } else if (ok && !token) {
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      // 현재 경로로 유지
      router.push(router.currentRoute.value.fullPath);
    }
  } else {
    router.push(router.currentRoute.value.fullPath);
  }
};

// 추천 업 버튼
const likeUpBtn = () => {
  const token = Cookies.get('accessToken');

  if (token) {
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

  if (token) {
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

// 목록으로 가기
const goListBtn = () => {
  router.push('/community');
}

// 게시글 수정
const modifyBtn = () => {
  const token = Cookies.get('accessToken');

  const ok = confirm("게시글을 수정하시겠습니까?");

  if (ok && token && (currentUser.value === boardWriter.value)) {
    router.push(`/community/board/${props.boardId}/modify`);
  } else {
    alert("접근 권한이 없습니다.");
  }
};

// 게시글 삭제
const deleteBtn = () => {
  const token = Cookies.get('accessToken');

  const ok = confirm("게시글을 삭제하시겠습니까?");

  if (ok && token) {
    axios
        .delete(`/api/community/board/${props.boardId}`, {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          alert(response.data);
          router.push('/community');

        })
        .catch((error) => {
          alert(error.response.data);
          router.push(router.currentRoute.value.fullPath);
        });
  } else {
    router.push(router.currentRoute.value.fullPath);
  }
};

// 댓글 등록,수정,삭제 시 결과가 바로 반응되게 하려고 함수로 만듦.
const reloadComment = () => {
  axios
      .get(`/api/community/board/${props.boardId}/comment`, {})
      .then((response) => {
        commentList.value = response.data;
      })
      .catch((error) => {
        router.replace("/community");
        alert(error.response.data.message);
      });
}

// 로그인한 회원 닉네임 가져오기
onMounted(() => {
  const token = Cookies.get('accessToken');

  if (token) {
    axios
        .get("/api/community/board/getWriter", {
          headers: {
            Authorization: token
          }
        })
        .then((response) => {
          currentUser.value = response.data;
        })
        .catch((error) => {
          console.log(error)
        });
  }
});

// 게시글 및 댓글 내용
onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 200);

  // 게시글 내용
  axios
      .get(`/api/community/board/${props.boardId}/get`, {})
      .then((response) => {
        board.value = response.data;
        boardWriter.value = response.data.writer;
      })
      .catch((error) => {
        console.log(error);
        router.replace("/community");
        alert(error.response.data.message);
      });

  // 댓글 내용
  axios
      .get(`/api/community/board/${props.boardId}/comment`, {})
      .then((response) => {
        commentList.value = response.data;
      })
      .catch((error) => {
        console.log(error);
        router.replace("/community");
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

.comment-div {
  width: 90%;
  height: 100px;
  padding: 10px;
  border: 3px solid #ccc;
  background-color: white;
}

.comment-textarea {
  width: 100%;
  height: 150px;
  padding: 10px;
  border: 1px solid #ccc;
}

.comment-button {
  width: 10%;
  padding: 5px 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
}

.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

</style>