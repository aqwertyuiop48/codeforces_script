# http_server.rb
require 'socket'

port = ENV.fetch("PORT", 5678).to_i
server = TCPServer.new port

puts "Running on PORT #{port}"

while session = server.accept
  request = session.gets
  puts request

  session.print "HTTP/1.1 200\r\n"
  session.print "Content-Type: text/html\r\n"
  session.print "\r\n"
    session.print(
    '''
    <h1>Hi Ruby World!</h1>
    '''
  )
  session.close
end
