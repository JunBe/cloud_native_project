document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("login");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // 폼의 기본 제출 동작 막기

        // 입력값 가져오기
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        try {
            // 로그인 요청
            const response = await fetch("http://192.168.152.128:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                }),
                credentials: "include"
            });

            if (response.ok) {
                // 로그인 성공
                const data = await response.text();
                alert("로그인 성공: " + data);
                window.location.href = "/account-book.html"; // 대시보드 페이지로 리다이렉트
            } else {
                // 로그인 실패
                alert("로그인 실패: " + (await response.text()));
            }
        } catch (error) {
            console.error("로그인 요청 중 오류 발생:", error);
            alert("로그인 중 오류가 발생했습니다.");
        }
    });
});
