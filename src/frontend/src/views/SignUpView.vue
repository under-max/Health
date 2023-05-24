<template>
    <div class="container">
        <el-form :model="form" :rules="rules" ref="loginForm" label-width="120px" class="custom-form">
            <el-form-item label="이메일" prop="email" required>
                <el-input v-model="form.email" placeholder="이메일 입력"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="checkEmailAvailability" type="primary">중복 검증</el-button>
            </el-form-item>
            <el-form-item label="비밀번호" prop="password" required>
                <el-input v-model="form.password" type="password" placeholder="비밀번호 입력"></el-input>
            </el-form-item>
            <el-form-item label="비밀번호 확인" prop="confirmPassword" required>
                <el-input v-model="form.confirmPassword" type="password" placeholder="비밀번호 다시입력"></el-input>
            </el-form-item>
            <el-form-item label="이름" prop="name" required>
                <el-input v-model="form.name" placeholder="닉네임 입력"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="checkNameAvailability" type="primary">중복 검증</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm">가입</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import router from "@/router";
import store from "@/stores/store";
import {showCustomAlert} from "@/main";
const form = ref({
    email: '',
    password: '',
    confirmPassword: '',
    name: '',
    // address: '',
    // phoneNumber: '',
    // idNumber: ''
})
const rules= {
        email: [
            {required: true, message: '이메일을 입력해주세요', trigger: 'blur'},
            {type: 'email', message: '유효한 이메일 형식이 아닙니다', trigger: 'blur'},
        ],
        password: [
            {required: true, message: '비밀번호를 입력해주세요', trigger: 'blur'},
            {min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다', trigger: 'blur'},
        ],
        confirmPassword: [
            {required: true, message: '비밀번호 확인을 입력해주세요', trigger: 'blur'},
            {validator: validateConfirmPassword, trigger: 'blur'},
        ],
        name: [
            {required: true, message: '닉네임을 입력해주세요', trigger: 'blur'},
        ],
    }

function validateConfirmPassword(rule, value, callback) {
    if (value !== form.value.password) {
        callback(new Error('비밀번호가 일치하지 않습니다'));
    } else {
        callback();
    }
}
const checkEmailAvailability = () => {
    axios
        .post('/api/auth/signup/email', {
            email: form.value.email,
        })
        .then((response) => {
            const valid = response.data.authResult;
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }

            if (valid) {
                showCustomAlert('사용 가능한 이메일입니다.');
            } else {
                showCustomAlert('중복된 이메일입니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });


}
const checkNameAvailability = () => {
    axios
        .post('/api/auth/signup/name', {
            name: form.value.name,
        })
        .then((response) => {
            const valid = response.data.authResult;
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }

            if (valid) {
                showCustomAlert('사용 가능한 닉네임입니다.');
            } else {
                showCustomAlert('중복된 닉네임입니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });


}
const submitForm = () => {
        if (loginFormRef.value)  {
            axios
                .post('/api/user', {
                    email: form.value.email,
                    password: form.value.password,
                    name: form.value.name,
                })
                .then(() => {
                    showCustomAlert("가입이 완료되었습니다.")
                    router.replace({name: "login"});
                })
                .catch((error) => {
                    if (error.response) {
                        const errorMessage = error.response.data.errors[0].defaultMessage;
                        showCustomAlert(`${errorMessage}`)
                    }
                });
        }
}
const loginFormRef = ref(null);
const loginForm = ref(null)
onMounted(() => {
    loginFormRef.value = loginForm;
})
</script>
<style scoped>
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

.custom-form {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.el-form-item__label {
    color: #333;
    font-weight: bold;
}

.el-input {
    width: 100%;
}

.el-button {
    margin-top: 20px;
}
</style>
