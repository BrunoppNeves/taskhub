export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center bg-gray-50 text-gray-800">
      <h1 className="text-4xl font-bold mb-4">Bem-vindo ao TaskHub</h1>
      <p className="text-lg text-gray-600 max-w-md text-center">
        Gerencie tarefas e otimize seu dia a dia com praticidade.
      </p>
      <div className="mt-8 flex gap-4">
        <a
          href="/login"
          className="rounded-lg bg-blue-600 px-6 py-3 text-white font-medium hover:bg-blue-700 transition"
        >
          Entrar
        </a>
        <a
          href="/register"
          className="rounded-lg border border-blue-600 px-6 py-3 text-blue-600 font-medium hover:bg-blue-50 transition"
        >
          Criar conta
        </a>
      </div>
    </main>
  );
}