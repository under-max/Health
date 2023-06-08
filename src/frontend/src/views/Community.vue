<template>
  <div class="container-lg">
    <h1>게시물 목록 - 작업중!!</h1>

    <div class="d-flex justify-content-between">

      <div>
        <button class="btn btn-info" @click="createBtn">
          <i class="fa-solid fa-pen"></i>
          작성하기
        </button>
      </div>

      <ul class="nav justify-content-center">
        <li class="nav-item">
          <RouterLink class="nav-link active" aria-current="page" to="/community">게시판</RouterLink>
        </li>
        <li class="nav-item">
          <RouterLink class="nav-link" to="/community/notice">공지사항</RouterLink>
        </li>
        <li class="nav-item">
          <RouterLink class="nav-link" to="/community/qna">1:1문의</RouterLink>
        </li>
      </ul>

      <!-- 게시글 order by -->
      <div class="dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown"
                :aria-expanded="isExpanded" @click="dropDownBtn">{{ selectedSort }}
        </button>
        <ul class="dropdown-menu" :class="{show: isExpanded}">
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('최신순')">최신순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('추천순')">추천순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('댓글순')">댓글순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('조회순')">조회순</button>
          </li>
        </ul>
      </div>

    </div>

    <div class="d-flex justify-content-center">
      <nav class="navbar bg-body-tertiary">
        <div class="container-fluid">
          <select class="form-select flex-grow-0" style="width: 100px;" name="type" v-model="searchType">
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="content">본문</option>
            <option value="writer">작성자</option>
          </select>
          <form class="d-flex" role="search">
            <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search"
                   v-model="searchKeyword">
            <button class="btn btn-success" type="button" @click="searchConditionBtn">search</button>
          </form>
        </div>
      </nav>
    </div>

    <table class="table table-hover">
      <thead>
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th><i class="fa-solid fa-binoculars"></i></th>
        <th><i class="fa-solid fa-bolt"></i></th>
        <th>작성일시</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="board in boardList">
        <td>{{ board.id }}</td>
        <td>
          <RouterLink :to="{ name: 'getBoard', params: {boardId: board.id}}"> {{ board.title }}</RouterLink>
        </td>
        <td>{{ board.writer }}</td>
        <td>2</td>
        <td>{{ board.likeCount }}</td>
        <td>{{ board.inserted }}</td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>


import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {RouterLink, useRouter} from "vue-router";
import Cookies from "vue-cookies";

const router = useRouter();

// 검색 키워드 관련
const searchType = ref('all');
const searchKeyword = ref('');

// dropdown 목록
const isExpanded = ref(false);

// 게시글 목록 리스트
const boardList = ref([]);

// 목록 초기값
const selectedSort = ref('최신순');

// 목록에서 선택한 값에 맞게 반환
const selectedSortLabel = computed(() => {
  switch (selectedSort.value) {
    case '최신순':
      return 'id';
    case '추천순':
      return 'like';
    case '댓글순':
      return 'comment';
    case '조회순':
      return 'view';
    default:
      return '';
  }
});

// 작성하기 버튼
const createBtn = () => {
  if (Cookies.get('accessToken')) {
    router.push('/community/new');
  } else {
    const ok = confirm("로그인이 필요합니다.");
    if (ok) {
      router.push('/login');
    } else {
      router.push('/community');
    }
  }
}

// dropdown 버튼
const dropDownBtn = () => {
  isExpanded.value = !isExpanded.value;
}

// 센터 검색 버튼
const searchConditionBtn = () => {
  axios
      .get("/api/community", {
        params: {
          type: searchType.value,
          keyword: searchKeyword.value,
          sort: selectedSortLabel.value
        }
      })
      .then((response) => {
        console.log(response.data);
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
}

// 게시글 순서선택
const selectSort = (sortValue) => {
  isExpanded.value = !isExpanded.value;
  selectedSort.value = sortValue;

  axios
      .get("/api/community", {
        params: {
          type: searchType.value,
          keyword: searchKeyword.value,
          sort: selectedSortLabel.value
        }
      })
      .then((response) => {
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
}

onMounted(() => {
  axios
      .get("/api/community", {
        params: {
          type: searchType.value,
          keyword: searchKeyword.value,
          sort: selectedSortLabel.value
        }
      })
      .then((response) => {
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});

</script>

<style scoped>

</style>