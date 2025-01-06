import React from "react";
import Header from "../components/header";
import { FaGithub } from "react-icons/fa"; // GitHub icon from react-icons

const AboutPage = () => {
  return (
    <div>
      <Header />  {/* Include the Header component */}
      <div className="about-page-container">
        <h1>About Us</h1>
        {/* Project Explanation Box */}
        <div className="project-box">
          <h2>Project Overview</h2>
          <p>
            This project focuses on implementing various encryption algorithms in a user-friendly interface.
            It includes techniques such as OTP encryption, Rail Fence cipher, and others. The goal is to
            provide an interactive platform for learning and applying these algorithms.
          </p>

          {/* GitHub Link */}
          <div className="github-link-container">
            <a
              href="https://github.com/your-repo-url"
              target="_blank"
              rel="noopener noreferrer"
              className="github-link"
            >
              <FaGithub className="icon" />
              <span>Check out the GitHub Repo</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AboutPage;
