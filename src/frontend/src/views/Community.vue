<template>
  <div class="container-lg">
    <h1>게시물 목록 - 작업중!!</h1>
    <a href="/member/signup">회원가입</a>
    게시물 수 : ${pageInfo['numOfRecords']}
    <form action="${pageLink }">

      <select name="maxPageSize">
        <option value="15">15개씩 보기</option>
      </select>
      <input type="submit" value="보기" />
    </form>

    <table class="table table-hover">
      <thead>
      <tr>
        <th>번호</th>
        <th>
          <i class="fa-solid fa-thumbs-up"></i>
        </th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>작성일시</th>
      </tr>
      </thead>
      <tbody>
        <tr>
          <td>${board.id }</td>
          <td>${board.likeCount }</td>
          <td>
            <a href="/id/${board.id }"> ${board.title } </a>
          </td>
          <td>${board.writer }</td>
          <td>${board.hit }</td>
          <td>${board.inserted }</td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="container-lg">
    <div class="row">
      <nav aria-label="Page navigation example">

        <ul class="pagination justify-content-center">



        </ul>
      </nav>
    </div>
  </div>
</template>

<script>
export default {
  name: "PostList",

  props: {
    boardName: '',
    postList: [],
    showQnA: false
  },

  setup() {
    const convertDateFormat = () => {

    }

  }
}
</script>

<style scoped>

</style>