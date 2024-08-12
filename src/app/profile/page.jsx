import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import UserSection from '@/app/components/sections/UserSection';
import { getServerSession } from 'next-auth/next';

export default async function Page() {
  return <UserSection />;
}
