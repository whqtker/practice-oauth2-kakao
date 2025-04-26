import { createContext, useContext, useState, ReactNode, useEffect } from 'react';
import axios from 'axios';
import { backUrl } from '../constants';
import {Member} from "../types/member.ts";

interface AuthContextType {
    isLoggedIn: boolean;
    userData: Member | null;
    login: () => void;
    logout: () => void;
    checkAuthStatus: () => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
    const [userData, setUserData] = useState<Member | null>(null);

    // 인증 상태 확인 함수
    const checkAuthStatus = async (): Promise<boolean> => {
        try {
            const response = await axios.get(`${backUrl}/api/v1/members/me`, {
                withCredentials: true,
            });

            const isAuthenticated = response.data && response.data.statusCode === 200;
            console.log("인증 상태:", response.data);

            setIsLoggedIn(isAuthenticated);

            if (isAuthenticated) {
                setUserData(response.data.data);
            } else {
                setUserData(null);
            }
            return isAuthenticated;
        } catch (error) {
            console.log(error)
            setIsLoggedIn(false);
            return false;
        }
    };

    const login = () => {
        setIsLoggedIn(true);
        setUserData(null);
    };

    const logout = async () => {
        try {
            // 백엔드 로그아웃 API가 있다면 호출
            await axios.delete(`${backUrl}/api/v1/members/logout`, { withCredentials: true });

            setIsLoggedIn(false);

            window.location.reload();

        } catch (error) {
            console.error('로그아웃 오류:', error);
        }
    };

    // 앱 시작 시 인증 상태 확인
    useEffect(() => {
        checkAuthStatus();
    }, []);

    return (
        <AuthContext.Provider value={{ isLoggedIn, login, logout, checkAuthStatus, userData }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}