<template>
    <div class="login-container">
        <form class="login-form">
            <div class="form-group">
                <label for="email">이메일</label>
                <input id="email" type="email" v-model="email"/>
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input id="password" type="password" v-model="password"/>
            </div>

            <span class="el-button" style="color: #1a1e21">
            <label for="keep">로그인정보저장 &nbsp;</label>
            <input type="checkbox" id="keep" v-model="keepLogin" model-value=true/>
            </span>
            <button class="btn" @click.prevent="login()">로그인</button>
            <div>
                <RouterLink style="color:white" to="/signup">
                    <button class="btn mt-2">
                        회원가입
                    </button>
                </RouterLink>
            </div>


        </form>

    </div>
</template>


<script>

</script>

<style>

</style>