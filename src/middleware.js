export { default } from 'next-auth/middleware';
export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - auth/signup (signup page)
     * - auth/signin (signin page)
     * - / (homepage)
     * - /games (games page)
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     * - fonts (fonts files)
     */
    '/((?!auth/signup|auth/signin||games|_next/static|_next/image|favicon.ico|fonts).*)',
  ],
};
