'use client';

import { useState } from "react";

export default function LoginPage() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    return(
        <main className="flex min-h-screen flex-col items-center justify-center bg-gray-50 text-gray-800"
        style={{ backgroundImage: "url('/background_login.jpg')", backgroundSize: 'cover' }}>
        <form className="bg-black/15 backdrop-blur-lg p-6 rounded-[2vw] shadow-md w-80">
        <h1 className="text-3xl font-bold mb-4 text-white text-center">Account Login</h1>

            <div className="mb-4">
                <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-200 leading-tight focus:outline-none focus:shadow-outline"
                    id="email"
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
            </div>
            <div className="mb-6">
                <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-200 leading-tight focus:outline-none focus:shadow-outline"
                    id="password"
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            <div className="flex items-center justify-center">
                <button
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-8 rounded-full focus:outline-none focus:shadow-outline"
                    type="submit"
                >
                    Sign in
                </button>
            </div>
        </form>


        </main>
    );
}