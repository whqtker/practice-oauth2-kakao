import { useEffect } from "react";
import { backUrl, frontUrl, kakaoUrl } from "../constants";
import { useAuth } from '../contexts/AuthContext';
import IconKakaoLargeWide from "./icons/IconKakaoLargeWide";

export function Home() {
  const { userData, checkAuthStatus, logout } = useAuth();

  useEffect(() => {
    checkAuthStatus();
  }, [checkAuthStatus]);

  const handleLogin = () => {    
    const encodedRedirectUrl = encodeURIComponent(`${frontUrl}/auth/kakao/callback`);
    const loginUrl = `${backUrl}${kakaoUrl}?redirectUrl=${encodedRedirectUrl}`;
    window.location.href = loginUrl;
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <div className="text-center">
          <img 
            src={userData?.avatar || "/default.jpg"}
            alt={`${userData?.nickname || "null"}'s avatar`}
            className="w-24 h-24 rounded-full mx-auto mb-4"
            onError={(e) => {
              e.currentTarget.src = '/default.jpg';
            }}
          />
          <h2 className="text-2xl font-bold mb-2">Welcome, {userData?.nickname || "null"}!</h2>
        </div>
        {!userData ? (
          <>
            <h2 className="text-2xl font-bold text-center mb-6">로그인</h2>
            <button
              onClick={handleLogin}
              className="w-full flex items-center justify-center gap-2 bg-[#FEE500] text-black py-3 rounded-lg hover:bg-[#FDD835] transition-colors"
            >
              <IconKakaoLargeWide />
            </button>
          </>
        ) : (
          <button
            onClick={logout}
            className="w-full mt-4 bg-red-500 text-white py-3 rounded-lg hover:bg-red-600 transition-colors"
          >
            로그아웃
          </button>
        )}
      </div>
    </div>
  );
}