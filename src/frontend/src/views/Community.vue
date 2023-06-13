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
          <router-link class="nav-link active" aria-current="page" to="/community">게시판</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/community/notice">공지사항</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/community/qna">1:1문의</router-link>
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
            <button class="btn btn-success" type="button" style="width: 100px;" @click="searchConditionBtn">검색</button>
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
        <th><i class="fa-regular fa-comment-dots"></i></th>
        <th><i class="fa-solid fa-bolt"></i></th>
        <th>작성일시</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="board in boardList">
        <td>{{ board.id }}</td>
        <td>
          <router-link :to="{ name: 'getBoard', params: {boardId: board.id}}">{{ board.title }}</router-link>
        </td>
        <td>{{ board.writer }}</td>
        <td>{{ board.viewCount }}</td>
        <td>{{ board.commentCount }}</td>
        <td>{{ board.likeCount }}</td>
        <td>{{ board.inserted }}</td>
      </tr>
      </tbody>
    </table>
  </div>
  

</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import axios from "axios";
import {RouterLink, useRoute, useRouter} from "vue-router";
import Cookies from "vue-cookies";

const router = useRouter();
const route = useRoute();

// 페이지 관련 정보
const pageInfo = ref({
  leftPageNumber: '',
  rightPageNumber: '',
  page: '',
  lastPageNumber: '',
  records: ''
});

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
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      router.push('/community');
    }
  }
};

// dropdown 버튼
const dropDownBtn = () => {
  isExpanded.value = !isExpanded.value;
};

// 센터 검색 버튼
const searchConditionBtn = () => {
  const query = {
    type: searchType.value,
    keyword: searchKeyword.value,
    sort: selectedSortLabel.value
  };

  axios
      .get(`/api/community`, {
        params: query
      })
      .then((response) => {
        console.log(response.data);
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
}

const selectSort = (sortValue) => {
  isExpanded.value = false;
  selectedSort.value = sortValue;

  // 게시글 순서선택
  const query = {
    type: searchType.value,
    keyword: searchKeyword.value,
    sort: selectedSortLabel.value
  };

  // 현재 URL에 쿼리 파라미터 추가 또는 변경
  router.push({query}).then(() => {
    // 목록 요청
    getBoardList();
  });
};

// watch를 사용하여 선택한 값이 변경될 때마다 sessionStorage에 저장
watch([selectedSort, searchType, searchKeyword], () => {
  sessionStorage.setItem("selectedSort", selectedSort.value);
  sessionStorage.setItem("searchType", searchType.value);
  sessionStorage.setItem("searchKeyword", searchKeyword.value);
});

const getBoardList = () => {
  // 게시글 순서선택
  const query = {
    type: searchType.value,
    keyword: searchKeyword.value,
    sort: selectedSortLabel.value
  };

  axios
      .get("/api/community", {
        params: route.query // 현재 URL의 쿼리 파라미터를 사용하여 요청
      })
      .then((response) => {
        console.log(response.data);
        boardList.value = response.data.list;
        pageInfo.value = response.data.pageInfo;
      })
      .catch((error) => {
        console.log(error);
        alert(error.response.data.message);
      });
};

onMounted(() => {
  // 뒤로가기 이벤트 감지
  window.onpopstate = function (event) {
    if (event.state == null) {
      selectedSort.value = sessionStorage.getItem("selectedSort") || "최신순";
      searchType.value = sessionStorage.getItem("searchType") || "all";
      searchKeyword.value = sessionStorage.getItem("searchKeyword") || "";

      getBoardList();
    }
  };

  getBoardList();
});

</script>

<style scoped>

</style>