/** @type {import('next').NextConfig} */

const nextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'cdn2.steamgriddb.com',
                port: '',
                pathname: '/grid/**',
            }
        ],
    },

};

export default nextConfig;