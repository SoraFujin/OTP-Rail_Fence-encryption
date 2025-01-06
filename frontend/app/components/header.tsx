import React from 'react';
import Link from 'next/link';
import Image from 'next/image';

const Header = () => {
    return (
        <header className="header">
            {/* Logo Section */}
            <div className="logo hover:text-red-600">
                <Link href="/">
                    <Image
                        src="/encryptionLogo.svg"
                        alt="encryption logo"
                        width={60}
                        height={60}
                    />
                </Link>
            </div>

            {/* Navigation Section */}
            <nav>
                <ul>
                    <li>
                        <Link href="/about" className="hover:text-red-600">
                            About
                        </Link>
                    </li>
                    <li>
                        <Link href="/otp" className="hover:text-red-600">
                            OTP Encryption
                        </Link>
                    </li>
                    <li>
                        <Link href="/rail-fence" className="hover:text-red-600">
                            Rail Fence
                        </Link>
                    </li>
                    <li>
                        <Link href="/both" className="hover:text-red-600">
                            Both
                        </Link>
                    </li>
                </ul>
            </nav>

            {/* Search Bar */}
            <div className="search-container">
                <input
                    type="text"
                    placeholder="Search..."
                    className="p-2 outline-none text-black placeholder-gray-500"
                />
                <button
                    type="submit"
                    className="bg-red-600 px-4 py-2 text-white font-semibold transition-all duration-300 hover:bg-red-700"
                >
                    Search
                </button>
            </div>
        </header>
    );
};

export default Header;
