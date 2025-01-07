import React from 'react';
import Link from 'next/link';
import Image from 'next/image';

const Header = () => {
    return (
        <header className="header">
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

            <div>
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
                    </ul>
                </nav>
            </div>
        </header>
    );
};

export default Header;
