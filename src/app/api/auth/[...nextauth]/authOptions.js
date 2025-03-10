import CredentialsProvider from 'next-auth/providers/credentials';

const INTERNAL_API_URL = process.env.NEXT_PUBLIC_INTERNAL_API_URL;
const EXTERNAL_API_URL = process.env.NEXT_PUBLIC_EXTERNAL_API_URL;

// Detects if its executing on client or in server
const API_URL = typeof window === 'undefined' ? INTERNAL_API_URL : EXTERNAL_API_URL;

export const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        username: { label: 'Username', type: 'text', placeholder: 'jsmith' },
        password: { label: 'Password', type: 'password' },
      },
      async authorize(credentials, req) {
        const res = await fetch(`${API_URL}/api/auth/login`, {
          method: 'POST',
          body: JSON.stringify(credentials),
          headers: { 'Content-Type': 'application/json' },
        });
        const user = await res.json();

        // If no error and we have user data, return it
        if (res.ok && user) {
          return user;
        }
        // Return null if user data could not be retrieved
        return null;
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user, trigger, session }) {
      if (trigger === 'update') {
        return { ...token, ...session.user };
      }
      return { ...token, ...user };
    },
    async session({ session, token, user }) {
      session.user = token;
      return session;
    },
  },
  secret: process.env.NEXTAUTH_SECRET,
  pages: {
    signIn: '/auth/signin',
    signOut: '/',
  },
};
