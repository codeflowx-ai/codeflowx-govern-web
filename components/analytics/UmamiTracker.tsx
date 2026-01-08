"use client";

import Script from 'next/script';

export default function UmamiTracker() {
  return (
    <Script
      defer
      src="https://analytics.codeflowx.cloud/script.js"
      data-website-id="edd21c8e-c20f-4bfe-b7f4-b5a7533fb722"
      strategy="afterInteractive"
    />
  );
}
